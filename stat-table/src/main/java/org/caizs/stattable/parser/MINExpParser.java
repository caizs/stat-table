package org.caizs.stattable.parser;

import com.lianfan.numas.kpi.domain.vo.KpiPerXmlTemplateVO;

/**
 * 计算如下占位符
 * {MIN数字} 表示第几列最小值
 */
public class MINExpParser extends ExpParser {

    @Override public String getRegExp() {
        return "(\\{MIN\\d+\\})";
    }

    @Override public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        Float value = context.getCachedValue(currentRecordId, "MIN" + column.getIndex());
        if (value != null) {
            return value;
        }
        ExpParser entryParser = ExpParserManager.getEntryParser();
        Float minValue = Float.MAX_VALUE;
        for (Integer uid : context.getRecordUserIds(currentRecordId)) {
            Float parseValue = entryParser.parse(currentRecordId, uid, column, context);
            if (parseValue.compareTo(minValue) < 0) {
                minValue = parseValue;
            }
        }
        context.setCachedValue(currentRecordId, "MIN" + column.getIndex(), minValue);
        return minValue;
    }
}
