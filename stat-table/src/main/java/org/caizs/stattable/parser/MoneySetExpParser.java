package org.caizs.stattable.parser;


import org.caizs.stattable.domain.TemplateVo;

/**
 * 计算如下占位符
 * {moneySet}表示页面设置的总金额
 */
public class MoneySetExpParser extends ExpParser {

    @Override public String getRegExp() {
        return "(\\{moneySet\\})";
    }

    @Override public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        return context.getMoneySet();
    }

}
