package org.caizs.stattable.service;


import org.caizs.stattable.domain.RecordDetail;
import org.caizs.stattable.domain.TemplateVo;
import org.caizs.stattable.parser.ExpParseContext;
import org.caizs.stattable.parser.ExpParser;
import org.caizs.stattable.parser.ExpParserManager;
import org.caizs.stattable.parser.StatisticsEntryExpParser;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Validated
@Service
public class RecordService {

    public void evaluate(Integer year, Integer month, Integer unitId) {
        ExpParseContext context = evaluateTableColumn(year, month, unitId);//计算表格列
        evaluateStatisticsColumn(context);//计算统计列
    }

    private ExpParseContext evaluateTableColumn(Integer year, Integer month, Integer unitId) {
        ExpParseContext context = initContext(year, month, unitId);
        List<TemplateVo.Column> kkColumns = collectColumns(context.getTemplate(), true).stream()
                                                                                       .filter(TemplateVo.Column::getStepColumn)
                                                                                       .collect(toList());
        ExpParser entryParser = ExpParserManager.getEntryParser();
        for (TemplateVo.Column column : kkColumns) {
            for (RecordDetail detail : context.getOriginalRecordDetails()) {
                entryParser.parse(detail.getRecordId(), detail.getUserId(), column, context);
            }
        }
        return context;
    }

    private void evaluateStatisticsColumn(ExpParseContext context) {
        List<TemplateVo.Column> columns = collectStatisticsColumns(context.getTemplate());
        context.initColumnMap(columns);
        StatisticsEntryExpParser entryParser = ExpParserManager.getStatiscsEntryExpParser();
        for (Integer recordId : context.getRecordIds()) {
            for (TemplateVo.Column column : columns) {
                entryParser.parse(recordId, null, column, context);
            }
        }
    }


}
