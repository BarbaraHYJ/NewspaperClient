package com.example.barbara.newspapersub.Model;

/**
 * 返回数据的封装类
 * 包含：状态码，返回实体
 */
public class ResponseVO {
    private int stateCode;
    private Object entity;

    public ResponseVO(int stateCode, Object entity) {
        this.stateCode = stateCode;
        this.entity = entity;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }
}
