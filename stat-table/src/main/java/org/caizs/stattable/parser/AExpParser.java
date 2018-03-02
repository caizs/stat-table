package org.caizs.stattable.parser;


import org.caizs.stattable.domain.TemplateVo;

/**
 * 计算如下占位符
 * {A数字} 表示第几列求和
 */
public class AExpParser extends ExpParser {

    @Override public String getRegExp() {
        return "(\\{A\\d+\\})";
    }

    @Override public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        Float value = context.getCachedValue(currentRecordId, "A" + column.getIndex());
        if (value != null) {
            return value;
        }
        ExpParser entryParser = ExpParserManager.getEntryParser();
        value = 0F;
        for (Integer uid : context.getRecordUserIds(currentRecordId)) {
            Float parseValue = entryParser.parse(currentRecordId, uid, column, context);
            value = value + parseValue;
        }
        context.setCachedValue(currentRecordId, "A" + column.getIndex(), value);
        return value;
    }


}
