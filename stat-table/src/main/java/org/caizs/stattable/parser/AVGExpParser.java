package org.caizs.stattable.parser;


import org.caizs.stattable.domain.TemplateVo;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;


/**
 * 计算如下占位符
 * {AVG数字} 表示第几列平均值
 */
public class AVGExpParser extends ExpParser {
    @Override public String getRegExp() {
        return "(\\{AVG\\d+\\})";
    }

    @Override public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        Float value = context.getCachedValue(currentRecordId, "AVG" + column.getIndex());
        if (value != null) {
            return value;
        }
        Float parseValue = ExpParserManager.getSumParser().parse(currentRecordId, userId, column, context);
        List<Integer> userIds = context.getRecordUserIds(currentRecordId);
        value = isEmpty(userIds) ? 0F : parseValue / userIds.size();
        context.setCachedValue(currentRecordId, "AVG" + column.getIndex(), value);
        return value;
    }
}
