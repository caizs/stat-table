package org.caizs.stattable.parser;

import com.lianfan.numas.kpi.domain.vo.KpiPerXmlTemplateVO;
import org.essentials4j.Do;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * 计算如下占位符
 * {RANK数字} 表示第几列当前行的排名名次，从小到大
 */
public class RANKExpParser extends ExpParser {

    @Override public String getRegExp() {
        return "(\\{RANK\\d+\\})";
    }

    @Override public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        Map<Integer, Integer> rankMap = context.getRankValue(currentRecordId, column.getIndex());
        if (rankMap != null) {
            return Float.valueOf(rankMap.get(userId));
        }
        List<ValueRank> values = new ArrayList<>();
        ExpParser entryParser = ExpParserManager.getEntryParser();
        for (Integer uid : context.getRecordUserIds(currentRecordId)) {
            Float parseValue = entryParser.parse(currentRecordId, uid, column, context);
            values.add(new ValueRank(uid, parseValue));
        }
        sort(values);
        rankMap = Do.map(values).toMap(ValueRank::getUserId, ValueRank::getRanking);
        context.setRankValue(currentRecordId, column.getIndex(), rankMap);
        setRRankValue(currentRecordId, column.getIndex(), values, context);
        return Float.valueOf(rankMap.get(userId));
    }

    private void setRRankValue(Integer currentRecordId, Integer index, List<ValueRank> values, ExpParseContext context) {
        ValueRank maxRank = values.stream().max(comparing(ValueRank::getRanking)).get();
        Map<Integer, Integer> rrankMap = values.stream().collect(
                Collectors.toMap(ValueRank::getUserId, v -> maxRank.getRanking() + 1 - v.getRanking()));
        context.setRRankValue(currentRecordId, index, rrankMap);
    }

//    public static void main(String[] args) {
//        List<ValueRank> values = New.list(new ValueRank(1, 1F), new ValueRank(2, 2F), new ValueRank(3, 2F), new ValueRank(4, 3F)
//                , new ValueRank(5, 3F), new ValueRank(6, 4F), new ValueRank(7, 5F));
//        System.out.println(values.stream().map(ValueRank::getValue).map(Float::intValue).collect(Collectors.toList()));
//
//        RANKExpParser parser = new RANKExpParser();
//        parser.sort(values);
//        Map<Integer, Integer> rankMap = Do.map(values).toMap(ValueRank::getUserId, ValueRank::getRanking);
//        System.out.println(JsonUtil.toJson(rankMap.values()));
//
//        ValueRank maxRank = values.stream().max(comparing(ValueRank::getRanking)).get();
//        Map<Integer, Integer> rrankMap = values.stream().collect(
//                Collectors.toMap(ValueRank::getUserId, v -> maxRank.getRanking() + 1 - v.getRanking()));
//        System.out.println(JsonUtil.toJson(rrankMap.values()));
//    }


    private void sort(List<ValueRank> values) {
        values.sort(comparing(ValueRank::getValue));

        Float former = values.get(0).getValue();
        int ranking = 1;
        for (int i = 0; i < values.size(); i++) {
            ValueRank vk = values.get(i);
            if (vk.getValue().compareTo(former) <= 0) {
                vk.setRanking(ranking);
            } else {
                ranking++;
                vk.setRanking(ranking);
            }
            former = vk.getValue();
        }
    }

    public static class ValueRank {
        Integer userId;
        Float value;
        Integer ranking;

        public ValueRank(Integer userId, Float value) {
            this.userId = userId;
            this.value = value;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Float getValue() {
            return value;
        }

        public void setValue(Float value) {
            this.value = value;
        }

        public Integer getRanking() {
            return ranking;
        }

        public void setRanking(Integer ranking) {
            this.ranking = ranking;
        }
    }
}
