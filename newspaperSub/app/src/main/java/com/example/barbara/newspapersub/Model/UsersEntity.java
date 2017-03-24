package com.example.barbara.newspapersub.Model;

import java.util.Collection;

/**
 * Created by barbara on 2016/12/15.
 */
public class UsersEntity {

    private int uId;
    private String uPassword;
    private String uName;
    private String uIdcard;
    private String uPhone;
    private String uAddress;
    private int dId;
    private Collection<OrdersEntity> ordersesByUId;

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuIdcard() {
        return uIdcard;
    }

    public void setuIdcard(String uIdcard) {
        this.uIdcard = uIdcard;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getuAddress() {
        return uAddress;
    }

    public void setuAddress(String uAddress) {
        this.uAddress = uAddress;
    }

    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public Collection<OrdersEntity> getOrdersesByUId() {
        return ordersesByUId;
    }

    public void setOrdersesByUId(Collection<OrdersEntity> ordersesByUId) {
        this.ordersesByUId = ordersesByUId;
    }
}
