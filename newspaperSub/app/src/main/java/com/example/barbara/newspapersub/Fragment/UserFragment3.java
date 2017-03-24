package com.example.barbara.newspapersub.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.barbara.newspapersub.Actitity.user.UpdatePwdActivity;
import com.example.barbara.newspapersub.Actitity.user.UpdateUserActivity;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.DepartmentEntity;
import com.example.barbara.newspapersub.Model.UsersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 用户信息显示
 */
public class UserFragment3 extends Fragment {

    private UsersEntity loginUser;
    private ArrayList<DepartmentEntity> departList;
    private RequestQueue mRequestQueue;
    private MyApplication mApplication;
    private TextView tvId, tvName, tvIdcard, tvPhone, tvDepart, tvAddress;
    private Button btnUpdateUser, btnUpdatePwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (MyApplication) getActivity().getApplication();
        loginUser = mApplication.getLoginUser();
        mRequestQueue = mApplication.getRequestQueue();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_3, container, false);

        tvId = (TextView) view.findViewById(R.id.tv_id);
        tvName = (TextView) view.findViewById(R.id.tv_nickname);
        tvIdcard = (TextView) view.findViewById(R.id.tv_idcard);
        tvPhone = (TextView) view.findViewById(R.id.tv_phone);
        tvDepart = (TextView) view.findViewById(R.id.tv_depart);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        btnUpdateUser = (Button) view.findViewById(R.id.bt_update_user);
        btnUpdatePwd = (Button) view.findViewById(R.id.bt_update_pwd);

        tvId.setText(String.valueOf(loginUser.getuId()));
        tvName.setText(loginUser.getuName());
        tvIdcard.setText(loginUser.getuIdcard());
        tvPhone.setText(loginUser.getuPhone());
        tvAddress.setText(loginUser.getuAddress());
        initDepartList();

        initListener();

        return view;
    }

    private void initListener() {
        btnUpdateUser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), UpdateUserActivity.class);
                        startActivity(intent);
                    }
                }
        );

        btnUpdatePwd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), UpdatePwdActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    /**
     * 初始化部门列表 Spinner
     */
    private void initDepartList() {
        String url = mApplication.DEPART_LIST_GET;
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(getActivity(), "获取部门列表失败", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(responseCode == 500) {
                                String listJson = response.getString("entity");
                                departList = (ArrayList)JSON.parseArray(listJson, DepartmentEntity.class);

                                String departName = null;
                                for(DepartmentEntity d : departList) {
                                    if(d.getdId() == loginUser.getdId()) {
                                        departName = d.getdName();
                                    }
                                }
                                if(departName != null) tvDepart.setText(departName);

                                mApplication.setDepartList(departList);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "获取部门列表失败", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        mRequestQueue.add(jsonRequest);
    }

}
