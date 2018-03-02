package org.caizs.stattable.parser;

import org.caizs.stattable.domain.TemplateVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * xml模板表达式解析
 */
public abstract class ExpParser {

    public Float innerParse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context) {
        List<String> holders = collectPlaceHolder(column.getStatExp());
        String statExp = column.getStatExp();
        for (String placeHolder : holders) {
            ExpParser parser = ExpParserManager.getMatchedParser(placeHolder);
            Float value = parser.parse(currentRecordId, userId, getColumn(placeHolder, context), context);
            statExp = statExp.replace(placeHolder, String.valueOf(value));
        }
        return evalMathExp(statExp);
    }

    private TemplateVo.Column getColumn(String placeHolder, ExpParseContext context) {
        Integer index = extractIndex(placeHolder);
        return index == null ? null : context.getColumn(index);//null为moneySet列
    }

    private Float evalMathExp(String expression) {
        return Float.valueOf(String.valueOf(MVEL.eval(expression, new HashMap(0))));
    }

    abstract public String getRegExp();

    /**
     * 占位符是否匹配
     */
    public boolean match(String exp) {
        Pattern pattern = Pattern.compile(getRegExp());
        return pattern.matcher(exp).find();
    }

    /**
     * @param currentRecordId
     * @param userId          计算的当前用户
     * @param column          计算的当前表达式列
     * @param context         计算上下文
     * @return
     */
    abstract public Float parse(Integer currentRecordId, Integer userId, TemplateVo.Column column,
            ExpParseContext context);

    /**
     * 是否跨科表达式列,
     * 所有跨科占位符均KK开头，如{KKA数字} 表示跨科 列求和
     */
    public static boolean isKKColumn(TemplateVo.Column column) {
        if (isStatColumn(column) && column.getStatExp().contains("KK")) {
            return true;
        }
        return false;
    }

    /**
     * 是否表达式列
     */
    public static boolean isStatColumn(TemplateVo.Column column) {
        if ("stat".equalsIgnoreCase(column.getType())) {
            return true;
        }
        return false;
    }

    /**
     * 是否sql列
     */
    public static boolean isSqlColumn(TemplateVo.Column column) {
        if ("sql".equalsIgnoreCase(column.getType())) {
            return true;
        }
        return false;
    }

    /**
     * 是否fix列
     */
    public static boolean isFixColumn(TemplateVo.Column column) {
        if ("fix".equalsIgnoreCase(column.getType())) {
            return true;
        }
        return false;
    }

    /**
     * 是否包含总金额列占位符
     */
    public static boolean containMoneySet(TemplateVo.Column column) {
        if (isStatColumn(column) && containMoneySet(column.getStatExp())) {
            return true;
        }
        return false;
    }

    public static boolean containMoneySet(String exp) {
        return exp.contains("{moneySet}");
    }

    /**
     * 占位符表达式是否包含形式均为{xxxindex},xxx为A-Z大写字母且不包含其他任何字符和空格
     */
    public static boolean statExpMatch(String statExp, Integer index) {
        String reg = "(\\{[A-Z]+" + index + "\\})";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(statExp).find();
    }

    public static String buildCExpHolder(Integer index) {
        return "C" + index;
    }

    public static List<String> collectPlaceHolder(String statExp) {
        String reg = "(\\{[A-Z]+\\d+\\})";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(statExp);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        if (containMoneySet(statExp)) {
            list.add("{moneySet}");
        }
        return list;
    }

    public static Integer extractIndex(String placeHolder) {
        String reg = "\\{[A-Z]+(\\d+)\\}";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(placeHolder);
        return matcher.find() ? Integer.valueOf(matcher.group(1)) : null;
    }

    public static void main(String[] args) {
        //        Integer index = 1;
        //        String reg = "(\\{[A-Z]+" + index + "\\})";
        //        Pattern pattern = Pattern.compile(reg);
        //        String s = "{A1}*{RANK1}";
        //        Matcher matcher = pattern.matcher(s);
        //        while (matcher.find()) {
        //            System.out.println(matcher.group());
        //        }

        //   System.out.println(collectPlaceHolder("({C1}+{C2})/{A1}"));
        System.out.println(extractIndex("{C123}"));


    }


}
