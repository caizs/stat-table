package org.caizs.stattable.parser;


import com.lianfan.numas.kpi.domain.vo.KpiPerXmlTemplateVO;

/**
 * 计算如下占位符
 * {MAX数字} 表示第几列最大值
 */
public class MAXExpParser extends ExpParser {

    @Override public String getRegExp() {
        return "(\\{MAX\\d+\\})";
    }

    @Override public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        Float value = context.getCachedValue(currentRecordId, "MAX" + column.getIndex());
        if (value != null) {
            return value;
        }
        ExpParser entryParser = ExpParserManager.getEntryParser();
        Float maxValue = Float.MIN_VALUE;
        for (Integer uid : context.getRecordUserIds(currentRecordId)) {
            Float parseValue = entryParser.parse(currentRecordId, uid, column, context);
            if (parseValue.compareTo(maxValue) > 0) {
                maxValue = parseValue;
            }
        }
        context.setCachedValue(currentRecordId, "MAX" + column.getIndex(), maxValue);
        return maxValue;
    }
}
