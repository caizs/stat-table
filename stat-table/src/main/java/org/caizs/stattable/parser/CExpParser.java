package org.caizs.stattable.parser;


import org.caizs.stattable.domain.TemplateVo;

/**
 * 计算如下占位符
 * {C数字} 表示当前行第几列
 */
public class CExpParser extends ExpParser {

    @Override public String getRegExp() {
        return "(\\{C\\d+\\})";
    }

    @Override public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        if (column == null) {
            throw new RuntimeException("占位符中的index不存在于模板列中");
        }
        Float value = context.getColumnValue(currentRecordId, userId, column.getIndex());
        if (value != null) {
            return value;
        }
        value = innerParse(currentRecordId, userId, column, context);
        context.setColumnValue(currentRecordId, userId, column.getIndex(), value);
        return value;
    }

    public static void main(String[] args) {
        String placeHolder = "{C12}";
        System.out.println(new CExpParser().match(placeHolder));
    }

}
