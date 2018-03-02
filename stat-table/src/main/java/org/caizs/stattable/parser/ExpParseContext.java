package org.caizs.stattable.parser;

import org.caizs.stattable.domain.RecordDetail;
import org.caizs.stattable.domain.TemplateVo;
import org.springframework.util.NumberUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class ExpParseContext implements Serializable {

    //原始表格数据
    private List<RecordDetail> originalRecordDetails;

    //表头
    private TemplateVo template;
    private Map<Integer, TemplateVo.Column> columnMap;

    //每次考核，即每个recordId对应的表格数据
    private Map<Integer, TableData> recordDatas;

    //总金额设置
    private Float moneySet;

    //用于缓存每个record中除{C数字}以外的所有占位符计算值，即缓存中间计算值
    private Map<Integer, Map<String, Float>> parseValueCache = new ConcurrentHashMap<>();

    //用于每个record的存储统计行值
    private Map<Integer, Map<String, Float>> statisticsValues = new ConcurrentHashMap<>();

    //用于每个record每列每个人排序缓存
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> rankValueCache = new ConcurrentHashMap<>();
    //用于每个record每列每个人反向排序缓存
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> rrankValueCache = new ConcurrentHashMap<>();

    //获取表格列值
    public Float getColumnValue(Integer recordId, Integer userId, Integer columnIndex) {
        Object value = this.recordDatas.get(recordId).getData().get(userId).get(ExpParser.buildCExpHolder(columnIndex));
        if (value != null && !NumberUtils.isCreatable(String.valueOf(value))) {
            throw new RuntimeException("跨科计算列非数值, index:[" + columnIndex + "], value:[" + value + "]");
        }
        return value == null ? null : Float.valueOf(String.valueOf(value));
    }

    //设置表格列值
    public ExpParseContext setColumnValue(Integer recordId, Integer userId, Integer columnIndex, Float value) {
        this.recordDatas.get(recordId).getData().get(userId).put(ExpParser.buildCExpHolder(columnIndex), value);
        return this;
    }

    //获取中间计算值
    public synchronized Float getCachedValue(Integer recordId, String key) {
        Map<String, Float> map = this.parseValueCache.get(recordId);
        if (map == null) {
            this.parseValueCache.put(recordId, new HashMap<>());
            return null;
        }
        return map.get(key);
    }

    //设置中间计算值
    public synchronized ExpParseContext setCachedValue(Integer recordId, String key, Float value) {
        Map<String, Float> map = this.parseValueCache.get(recordId);
        if (map == null) {
            map = new HashMap<>();
            this.parseValueCache.put(recordId, map);
        }
        map.put(key, value);
        return this;
    }

    //设置统计值
    public synchronized ExpParseContext setStatisticsValue(Integer recordId, String key, Float value) {
        Map<String, Float> map = this.statisticsValues.get(recordId);
        if (map == null) {
            map = new HashMap<>();
            this.statisticsValues.put(recordId, map);
        }
        map.put(key, value);
        return this;
    }

    //获取统计值
    public synchronized Float getStatisticsValue(Integer recordId, String key) {
        Map<String, Float> map = this.statisticsValues.get(recordId);
        if (map == null) {
            this.statisticsValues.put(recordId, new HashMap<>());
            return null;
        }
        return map.get(key);
    }

    //获取排序值
    public synchronized Map<Integer, Integer> getRankValue(Integer recordId, Integer index) {
        Map<Integer, Map<Integer, Integer>> map = this.rankValueCache.get(recordId);
        if (map == null) {
            this.rankValueCache.put(recordId, new HashMap<>());
            return null;
        }
        return map.get(index);
    }

    //设置排序值
    public synchronized ExpParseContext setRankValue(Integer recordId, Integer index, Map<Integer, Integer> value) {
        Map<Integer, Map<Integer, Integer>> map = this.rankValueCache.get(recordId);
        if (map == null) {
            map = new HashMap<>();
            this.rankValueCache.put(recordId, map);
        }
        map.put(index, value);
        return this;
    }

    //获取反向排序值
    public synchronized Map<Integer, Integer> getRRankValue(Integer recordId, Integer index) {
        Map<Integer, Map<Integer, Integer>> map = this.rrankValueCache.get(recordId);
        if (map == null) {
            this.rrankValueCache.put(recordId, new HashMap<>());
            return null;
        }
        return map.get(index);
    }

    //设置反向排序值
    public synchronized ExpParseContext setRRankValue(Integer recordId, Integer index, Map<Integer, Integer> value) {
        Map<Integer, Map<Integer, Integer>> map = this.rrankValueCache.get(recordId);
        if (map == null) {
            map = new HashMap<>();
            this.rrankValueCache.put(recordId, map);
        }
        map.put(index, value);
        return this;
    }

    //获取record内所有userId
    public List<Integer> getRecordUserIds(Integer recordId) {
        return new ArrayList<>(this.recordDatas.get(recordId).getData().keySet());
    }

    //获取所有record的id
    public List<Integer> getRecordIds() {
        return new ArrayList<>(this.recordDatas.keySet());
    }

    public TemplateVo.Column getColumn(Integer index) {
        return this.columnMap.get(index);
    }


    public Map<Integer, Map<String, Float>> getParseValueCache() {
        return parseValueCache;
    }

    public void setParseValueCache(Map<Integer, Map<String, Float>> parseValueCache) {
        this.parseValueCache = parseValueCache;
    }

    public static class TableData {
        //原始表格数据，每个用户的表格数据
        Map<Integer, Map<String, Object>> data;

        public TableData(Map<Integer, Map<String, Object>> data) {
            this.data = data;
        }

        public Map<Integer, Map<String, Object>> getData() {
            return data;
        }

        public void setData(Map<Integer, Map<String, Object>> data) {
            this.data = data;
        }
    }

    public ExpParseContext(List<RecordDetail> details, TemplateVo template,
            List<TemplateVo.Column> columns, Map<Integer, TableData> recordDatas, Float moneySet) {
        CheckUtil.checkExist(template, "模板");
        CheckUtil.checkEmpty(columns, "模板列");
        this.originalRecordDetails = details;
        this.template = template;
        this.recordDatas = recordDatas;
        this.moneySet = moneySet;
        this.columnMap = columns.stream().filter(col -> col.getIndex() != null)
                                .collect(Collectors.toMap(TemplateVo.Column::getIndex, identity()));
    }

    public void initColumnMap(List<TemplateVo.Column> columns) {
        this.columnMap = columns.stream().filter(col -> col.getIndex() != null)
                                .collect(Collectors.toMap(TemplateVo.Column::getIndex, identity()));
    }

    public Map<Integer, Map<String, Float>> getStatisticsValues() {
        return statisticsValues;
    }

    public void setStatisticsValues(Map<Integer, Map<String, Float>> statisticsValues) {
        this.statisticsValues = statisticsValues;
    }

    public Float getMoneySet() {
        return moneySet;
    }

    public void setMoneySet(Float moneySet) {
        this.moneySet = moneySet;
    }

    public TemplateVo getTemplate() {
        return template;
    }

    public void setTemplate(TemplateVo template) {
        this.template = template;
    }

    public Map<Integer, TableData> getRecordDatas() {
        return recordDatas;
    }

    public Map<Integer, TemplateVo.Column> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<Integer, TemplateVo.Column> columnMap) {
        this.columnMap = columnMap;
    }

    public void setRecordDatas(
            Map<Integer, TableData> recordDatas) {
        this.recordDatas = recordDatas;
    }

    public List<RecordDetail> getOriginalRecordDetails() {
        return originalRecordDetails;
    }

    public void setOriginalRecordDetails(List<RecordDetail> originalRecordDetails) {
        this.originalRecordDetails = originalRecordDetails;
    }
}
