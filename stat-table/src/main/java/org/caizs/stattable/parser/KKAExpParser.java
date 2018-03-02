package org.caizs.stattable.parser;

import com.lianfan.numas.kpi.domain.vo.KpiPerXmlTemplateVO;

/**
 * 计算如下占位符
 * {KKA数字} 表示跨科 列求和
 */
public class KKAExpParser extends ExpParser {

    @Override public String getRegExp() {
        return "(\\{KKA\\d+\\})";
    }

    @Override public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        Float value = context.getCachedValue(currentRecordId, "KKA" + column.getIndex());
        if (value != null) {
            return value;
        }
        ExpParser sumParser = ExpParserManager.getSumParser();
        value = 0F;
        for (Integer recId : context.getRecordIds()) {
            Float parseValue = sumParser.parse(recId, userId, column, context);
            value = value + parseValue;
        }
        context.setCachedValue(currentRecordId, "KKA" + column.getIndex(), value);
        return value;
    }
}
