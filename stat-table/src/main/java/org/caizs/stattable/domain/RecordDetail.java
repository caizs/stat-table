package org.caizs.stattable.domain;

import java.util.Date;

public class RecordDetail {
    private Integer id;
    private Integer recordId;
    private Integer userId;
    //员工类型
    private String empType;
    //考评用户所在科室id
    private Integer userUnitId;
    //考评用户得分json, toJson(List<Pair>), pair表示键值对
    private String dataJson;
    private Date postTime;

    public RecordDetail() {
    }

    public RecordDetail(Integer userId, String empType, Integer userUnitId, String dataJson) {
        this.userId = userId;
        this.empType = empType;
        this.userUnitId = userUnitId;
        this.dataJson = dataJson;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserUnitId() {
        return userUnitId;
    }

    public void setUserUnitId(Integer userUnitId) {
        this.userUnitId = userUnitId;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }
}
