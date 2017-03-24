package com.example.barbara.newspapersub.Contents;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.barbara.newspapersub.Model.AdminuserEntity;
import com.example.barbara.newspapersub.Model.DepartmentEntity;
import com.example.barbara.newspapersub.Model.NewspaperclassEntity;
import com.example.barbara.newspapersub.Model.UsersEntity;

import java.util.ArrayList;

public class MyApplication extends Application {

    private UsersEntity loginUser = new UsersEntity();
    private AdminuserEntity loginAdmin = new AdminuserEntity();
    private ArrayList<NewspaperclassEntity> newspaperclassList;
    private ArrayList<DepartmentEntity> departList;

    private RequestQueue requestQueue;

    public UsersEntity getLoginUser() {
        return loginUser;
    }

    public void userLogin(UsersEntity user) {
        loginUser.setuId(user.getuId());
        loginUser.setuPassword(user.getuPassword());
        loginUser.setuName(user.getuName());
        loginUser.setuIdcard(user.getuIdcard());
        loginUser.setuPhone(user.getuPhone());
        loginUser.setuAddress(user.getuAddress());
        loginUser.setdId(user.getdId());
        loginUser.setOrdersesByUId(user.getOrdersesByUId());
    }

    public void userLogout() {
        loginUser = new UsersEntity();
    }

    public void setNewspaperclassList(ArrayList list) {
        newspaperclassList = list;
    }

    public void adminLogout() {
        loginAdmin = new AdminuserEntity();
    }

    public void adminLogin(AdminuserEntity adminuserEntity) {
        loginAdmin.setaName(adminuserEntity.getaName());
        loginAdmin.setaPassword(adminuserEntity.getaPassword());
    }

    public AdminuserEntity getLoginAdmin() {
        return loginAdmin;
    }

    public ArrayList getNewspaperclassList() {
        return newspaperclassList;
    }

    public ArrayList<DepartmentEntity> getDepartList() {
        return departList;
    }

    public void setDepartList(ArrayList<DepartmentEntity> departList) {
        this.departList = departList;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    //    public String IP = "http://192.168.1.134:8080/";
    public String IP = "http://119.29.28.177:8080/newspaperSub/";
    //user
    public String USER_LOGIN = IP + "UserController/login";//+{id}/{pwd}
    public String USER_UPDATE = IP + "UserController/update";//post
    public String USER_ADD = IP + "UserController/add";//post
    public String USER_DELETE_BY_ID = IP + "UserController/delete";//+{id}
    public String USER_LIST_GET = IP + "UserController/users";
    public String USER_GET_BY_ID = IP + "UserController/user";//+{id}
    //admin
    public String ADMIN_LOGIN = IP + "AdminuserController/login"; //+{aName}/{aPassword}
    //depart
    public String DEPART_ADD = IP + "DepartmentController/addDepartment"; //post
    public String DEPART_DELETE = IP + "DepartmentController/deleteDepartment"; //+{id}
    public String DEPART_LIST_GET = IP + "DepartmentController/getList";
    public String DEPART_GET_BY_ID = IP + "DepartmentController/department";//+{id}
    //newsClass
    public String CLASS_LIST_GET = IP + "NewspaperclassController/classes";
    public String CLASS_GET_BY_ID = IP + "NewspaperclassController/class";//+{id}
    //newspaper
    public String NEWS_LIST_GET = IP + "NewspaperController/newspapers";
    public String NEWS_ADD = IP + "NewspaperController/addNewspaper"; //post
    public String NEWS_GET_BY_ID = IP + "NewspaperController/newspaper";//+{id}
    //order
    public String ORDER_ADD = IP + "OrdersController/addOrder";//post
    public String ORDER_LIST_GET_BY_USER = IP + "OrdersController/ordersByUser";//+{user_id}
    public String ORDER_LIST_GET_BY_NEWS = IP + "OrdersController/ordersByNewspaper";//+{news_id}
    public String ORDER_LIST_GET_BY_DEPART = IP + "OrdersController/ordersByUserDId/"; //+{depart_id}

    public String ORDER_SUM_BY_USER = IP + "OrdersController/copiesAndMonthSumByUId"; //+{user_id}
    public String ORDER_SUM_BY_NEWS = IP + "OrdersController/copiesAndMonthSumByNId"; //+{news_id}
    public String ORDER_SUM_BY_DEPART = IP + "OrdersController/copiesAndMonthSumByDId"; //+{depart_id}
}
