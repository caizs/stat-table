# stat-table
  - 通过配置模板文件实现表格计算，支持表格列计算、行计算和统计行
  - 可自定义实现列计算函数，扩展表格列计算方式
  
  
  ## 模板文件说明：
```xml
<?xml version="1.0" encoding="UTF-8"?><table>

    <header>
        <column index="1" name="姓名" value="{user.name}" type="fix" sort="1" width="20" height="10" fontSize="10" fontColor="#6495ED" />
        <column index="2" name="工号" value="{user.code}" type="fix"  />
        <column index="3" name="层级" value="{user.rank}" type="fix" display="false" />
        <column index="4" name="单元" value="{unit.name}" type="fix"  />
        
        <column name="岗位奖金（60%）" >
            <column index="6" name="岗位级别" type="manual" format="0.00"  />
            <column index="7" name="岗位天数" type="manual" format="0.00"   />
            <column index="8" name="跨科列" type="sql" format="0.00" sqlExp="SELECT id AS user_id, 1 AS value FROM plt_user WHERE id IN ({userIds})"  />
            <column index="9" name="岗位系数" type="stat" format="0.00"  statExp="{KKA8}" />
            <column index="10" name="岗位奖金" type="stat" format="0.0" statExp="{C9}*2"  />
        </column>
        <column index="15" name="总奖金" type="stat" format="0.0" statExp="{C9}*0.5+{C10}*0.5" tag="总奖金" />
        <column index="16" name="总得分" type="stat" format="0.0" statExp="{C9}*0.5+{C10}*0.5" tag="总得分" />
		
    </header>

    <statistics>
        <statcolumn index="16" format="0.00" statExp="{moneySet}"   />
    </statistics>

</table>

<table>

    <header>
        <column index="1" name="姓名" value="{user.name}" type="fix" sort="1" width="20" height="10" fontSize="10" fontColor="#6495ED" />
        <column index="2" name="工号" value="{user.code}" type="fix"  />
        <column index="3" name="层级" value="{user.rank}" type="fix" display="false" />
        <column index="4" name="单元" value="{unit.name}" type="fix"  />
        
        <column name="岗位奖金（60%）" >
            <column index="6" name="岗位级别" type="manual" format="0.00"  />
            <column index="7" name="岗位天数" type="manual" format="0.00"   />
            <column index="8" name="跨科列" type="sql" format="0.00" sqlExp="SELECT id AS user_id, 1 AS value FROM plt_user WHERE id IN ({userIds})"  />
            <column index="9" name="岗位系数" type="stat" format="0.00"  statExp="{KKA8}" />
            <column index="10" name="岗位奖金" type="stat" format="0.0" statExp="{C9}*2"  />
        </column>
        <column index="15" name="总奖金" type="stat" format="0.0" statExp="{C9}*0.5+{C10}*0.5" tag="总奖金" />
        <column index="16" name="总得分" type="stat" format="0.0" statExp="{C9}*0.5+{C10}*0.5" tag="总得分" />
		
    </header>

    <statistics>
        <statcolumn index="16" format="0.00" statExp="{moneySet}"   />
    </statistics>

</table>


<!-- column表示列表头，可以嵌套，表示表头合并
注：以下必填只针对数据列，不包括合并表头，合并表头只有name必填

index表示第几列，唯一标识，不可修改，必填
name表示列名称，必填

type表示类型：必填
  fix: 固定列，默认不可编辑
  manual: 手动输入列，默认可编辑
  sql: sql后台填充列，默认不可编辑
  stat: 占位符表达式统计列，默认不可编辑

sqlExp:sql语句，当type为sql时，必填

statExp:统计表达式，当type为stat时，必填，支持常用四则表达式，占位符格式如{C2}，标识column标签中的index为2的列值，{A8}表示第8列全部相加求和

value:固定列属性值，当type为fix时，必填，目前支持{user.name}、{user.code}、{user.rank}、{unit.name}等

editable:是否可编辑，可选，true/false，提供额外的编辑控制，默认情况列是否可编辑由type确定

format:显示格式，用于控制数值显示精度，可选，如0.00 表示两位小数，0.0000表示4位小数

display:是否显示，可选，默认true

tag:列内容标识，可选，包括“奖金”、“得分”、“常规”、“总奖金”、“总得分”

sort:显示顺序，可选，1,2,3...

width:显示列宽，可选，1,2,3...

height:显示列高，可选，1,2,3...

fontSize:字体大小，可选，1,2,3...

fontColor:字体颜色，可选，#6495ED

-->

<!--statcolumn 表示统计行，用于列统计，可选
index同上
format同上
statExp同上
-->

<!-- sqlExp表达式示例
sql表达式通过指定用户id来计算值，如下：

SELECT id AS user_id, id AS value
FROM plt_user WHERE id IN ({userIds})

返回列包含两个值，名称必须为user_id和value
同时用户id的占位符为{userIds}
-->

<!-- 所有占位符,形式均为{xxx数字},xxx为A-Z大写字母且不包含其他任何字符和空格

{C数字} 表示当前行第几列
{A数字} 表示第几列求和
{AVG数字} 表示第几列平均值
{MAX数字} 表示第几列最大值
{MIN数字} 表示第几列最小值
{RANK数字} 表示第几列当前行的排名名次，从小到大
{RRANK数字} 表示第几列当前行的排名名次，从大到小
{KKA数字} 表示跨科 列求和 

{moneySet}表示页面设置的总金额

column标签中type为fix的占位符
{user.name} 用户姓名
{user.code} 用户code
{user.rank} 用户层级
{unit.name} 科室名称

sqlExp中的占位符
{userIds} 表示用户id的集合
{year} 表示查询日期年份，如2017
{month} 表示查询日期月份, 如7
{unitId} 表示查询科室


-->



```
