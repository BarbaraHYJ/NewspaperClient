package com.example.barbara.newspapersub.Model;

/**
 * Created by barbara on 2016/12/15.
 */
public class OrdersEntity {

    private int oId;
    private int uId;
    private int nId;
    private int oNum;
    private int oMonth;

//    public OrdersEntity(int oId, int uId, int nId, int oNum, int oMonth) {
//        this.oId = oId;
//        this.uId = uId;
//        this.nId = nId;
//        this.oNum = oNum;
//        this.oMonth = oMonth;
//    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getnId() {
        return nId;
    }

    public void setnId(int nId) {
        this.nId = nId;
    }

    public int getoNum() {
        return oNum;
    }

    public void setoNum(int oNum) {
        this.oNum = oNum;
    }

    public int getoMonth() {
        return oMonth;
    }

    public void setoMonth(int oMonth) {
        this.oMonth = oMonth;
    }
}
