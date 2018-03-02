package org.caizs.stattable.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lianfan.nursecare.common.model.ExportColumn;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "table")
public class TemplateVo {

    private Header header;

    private Statistics statistics;

    public TemplateVo() {
    }

    public TemplateVo(Header header, Statistics statistics) {
        this.header = header;
        this.statistics = statistics;
    }

    @XmlElement
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @XmlElement
    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public static class Header {
        List<Column> column;

        public Header() {
        }

        public Header(List<Column> column) {
            this.column = column;
        }

        @XmlElement
        public List<Column> getColumn() {
            return column;
        }

        public void setColumn(List<Column> column) {
            this.column = column;
        }
    }

    public static class Column extends ExportColumn {
        //表示第几列，必填
        private Integer index;
        //表示列名称，必填
        private String name;
        /*type表示类型：必填
            fix: 固定列，code固定，默认不可编辑
            manual: 手动输入列，默认可编辑
            sql: sql后台填充列，默认不可编辑
            stat: 占位符表达式统计列，默认不可编辑
        */
        private String type;
        //sql语句，当type为sql时，必填
        private String sqlExp;
        //统计表达式，当type为stat时，必填，支持常用四则表达式，占位符格式如{C2}，标识column标签中的index为2的列值，{A8}表示第8列全部相加求和
        private String statExp;
        //固定列属性值，当type为fix时，必填，目前支持{user.name}、{user.code}、{user.rank}、{unit.name}等
        private String value;
        //是否可编辑，可选，true/false，提供额外的编辑控制，默认情况列是否可编辑由type确定
        private Boolean editable;
        //显示格式，用于控制数值显示精度，可选，如0.00 表示两位小数，0.0000表示4位小数
        private String format;
        //是否显示，可选，默认true
        private Boolean display;
        //列内容标识，可选，包括“奖金”、“得分”、“常规”、“总奖金”、“总得分”
        private String tag;
        //显示顺序，可选，1,2,3...
        private Integer sort;
        //显示列宽，可选，1,2,3...
        private Double width;
        //显示列高，可选，1,2,3...
        private Double height;
        //字体大小，可选，1,2,3...
        private Integer fontSize;
        //字体颜色，可选，#6495ED
        private String fontColor;
        //子列
        List<Column> children;

        /**
         * 是否跨列计算,默认false
         */
        private boolean stepColumn;

        public Column() {
        }

        public Column(Integer index, String name, String value, String type, String sqlExp, String statExp, Boolean editable,
                String format, Boolean display, String tag, Integer sort, Double width, Double height, Integer fontSize,
                String fontColor, List<Column> children) {
            this.index = index;
            this.name = name;
            this.value = value;
            this.type = type;
            this.sqlExp = sqlExp;
            this.statExp = statExp;
            this.editable = editable;
            this.format = format;
            this.display = display;
            this.tag = tag;
            this.sort = sort;
            this.width = width;
            this.height = height;
            this.fontSize = fontSize;
            this.fontColor = fontColor;
            this.children = children;
        }

        public boolean getStepColumn() {
            return this.stepColumn;
        }

        public void setStepColumn(boolean stepColumn) {
            this.stepColumn = stepColumn;
        }

        @Override public Boolean getDefaultDisplay() {
            return this.display != null ? display : true;
        }

        @XmlAttribute
        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        @XmlAttribute
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlAttribute
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @XmlAttribute
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @XmlAttribute
        public String getSqlExp() {
            return sqlExp;
        }

        public void setSqlExp(String sqlExp) {
            this.sqlExp = sqlExp;
        }

        @XmlAttribute
        public String getStatExp() {
            return statExp;
        }

        public void setStatExp(String statExp) {
            this.statExp = statExp;
        }

        @XmlAttribute
        public Boolean getEditable() {
            return editable;
        }

        public void setEditable(Boolean editable) {
            this.editable = editable;
        }

        @XmlAttribute
        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        @XmlAttribute
        public Boolean getDisplay() {
            return display;
        }

        public void setDisplay(Boolean display) {
            this.display = display;
        }

        @XmlAttribute
        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        @XmlAttribute
        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        @XmlAttribute
        public Double getWidth() {
            return width;
        }

        public void setWidth(Double width) {
            this.width = width;
        }

        @XmlAttribute
        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

        @XmlAttribute
        public Integer getFontSize() {
            return fontSize;
        }

        public void setFontSize(Integer fontSize) {
            this.fontSize = fontSize;
        }

        @XmlAttribute
        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }

        @XmlElement(name = "column")
        public List<Column> getChildren() {
            return children;
        }

        public void setChildren(List<Column> children) {
            this.children = children;
        }

        @JsonIgnore
        @Override public String getColumnName() {
            return this.name;
        }

        @JsonIgnore
        @Override public List<? extends ExportColumn> getColumnChildren() {
            return this.children;
        }

        @JsonIgnore
        @Override public String getColumnKey() {
            return this.index != null ? "C" + this.index : null;
        }
    }

    public static class Statistics {
        private List<StatColumn> statcolumn;

        public Statistics() {
        }

        public Statistics(List<StatColumn> statcolumn) {
            this.statcolumn = statcolumn;
        }

        @XmlElement
        public List<StatColumn> getStatcolumn() {
            return statcolumn;
        }

        public void setStatcolumn(List<StatColumn> statcolumn) {
            this.statcolumn = statcolumn;
        }
    }

    public static class StatColumn extends TemplateVo.Column {
        private Integer index;
        private String format;
        private String statExp;

        public StatColumn() {
        }

        public StatColumn(Integer index, String format, String statExp) {
            this.index = index;
            this.format = format;
            this.statExp = statExp;
        }

        @XmlAttribute
        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        @XmlAttribute
        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        @XmlAttribute
        public String getStatExp() {
            return statExp;
        }

        public void setStatExp(String statExp) {
            this.statExp = statExp;
        }
    }

}
