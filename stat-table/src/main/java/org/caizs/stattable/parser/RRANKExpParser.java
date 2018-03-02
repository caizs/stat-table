package org.caizs.stattable.parser;

import com.lianfan.numas.kpi.domain.vo.KpiPerXmlTemplateVO;

import java.util.Map;

/**
 * 计算如下占位符
 * {RRANK数字} 表示第几列当前行的排名名次，从大到小
 */
public class RRANKExpParser extends ExpParser {

    @Override public String getRegExp() {
        return "(\\{RRANK\\d+\\})";
    }

    @Override public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        Map<Integer, Integer> rankMap = context.getRRankValue(currentRecordId, column.getIndex());
        if (rankMap != null) {
            return Float.valueOf(rankMap.get(userId));
        }
        ExpParserManager.getRankParser().parse(currentRecordId, userId, column, context);
        rankMap = context.getRRankValue(currentRecordId, column.getIndex());
        return Float.valueOf(rankMap.get(userId));
    }


}
