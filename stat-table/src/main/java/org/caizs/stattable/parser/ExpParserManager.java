package org.caizs.stattable.parser;

import java.util.ArrayList;
import java.util.List;

public class ExpParserManager {

    private static final List<ExpParser> parsers = new ArrayList<>();
    private static final ExpParser entryParser = new CExpParser();//解析入口
    private static final ExpParser sumParser = new AExpParser();//求和解析
    private static final ExpParser rankParser = new RANKExpParser();//排序解析
    private static final StatisticsEntryExpParser statiscsEntryExpParser = new StatisticsEntryExpParser();//用于统计行解析

    static {
        parsers.add(entryParser);
        parsers.add(sumParser);
        parsers.add(rankParser);
        parsers.add(new RRANKExpParser());
        parsers.add(new KKAExpParser());
        parsers.add(new MoneySetExpParser());
        parsers.add(new AVGExpParser());
        parsers.add(new MAXExpParser());
        parsers.add(new MINExpParser());

        //...
    }

    public static ExpParser getMatchedParser(String placeHolder) {
        for (ExpParser parser : parsers) {
            if (parser.match(placeHolder)) {
                return parser;
            }
        }
        throw new RuntimeException("模板占位符匹配失败:[" + placeHolder + "]");
    }


    public static ExpParser getEntryParser() {
        return ExpParserManager.entryParser;
    }

    public static ExpParser getSumParser() {
        return ExpParserManager.sumParser;
    }

    public static StatisticsEntryExpParser getStatiscsEntryExpParser() {
        return ExpParserManager.statiscsEntryExpParser;
    }

    public static ExpParser getRankParser() {
        return ExpParserManager.rankParser;
    }

    private ExpParserManager() {

    }
}
