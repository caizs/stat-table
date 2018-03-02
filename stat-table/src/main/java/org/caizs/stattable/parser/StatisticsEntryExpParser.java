package org.caizs.stattable.parser;


import org.caizs.stattable.domain.TemplateVo;

/**
 * 用于统计行解析
 */
public class StatisticsEntryExpParser {

    public void parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        Float value = context.getStatisticsValue(currentRecordId, "C" + column.getIndex());
        if (value != null) {
            return;
        }
        value = ExpParserManager.getEntryParser().innerParse(currentRecordId, userId, column, context);
        context.setStatisticsValue(currentRecordId, "C" + column.getIndex(), value);
    }
}
