package com.example.barbara.newspapersub.Actitity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.barbara.newspapersub.Actitity.LoginActivity;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.DepartmentEntity;
import com.example.barbara.newspapersub.Model.UsersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by barbara on 2016/12/23.
 */
public class RegisterActivity extends Activity {

    private ArrayList<DepartmentEntity> departList;
    private RequestQueue mRequestQueue;
    private MyApplication mApplication;
    private EditText etPwd, etName, etIdcard, etPhone, etAddress;
    private Spinner spDepart;
    private Button btnOk;

    ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mApplication = (MyApplication)getApplication();
        mRequestQueue = mApplication.getRequestQueue();

        initView();
        initListener();
    }

    /**
     * 初始化ok监听器
     */
    private void initListener() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*内容检查*/
                if (etName.length() == 0 || etPwd.length() == 0 || etIdcard.length() == 0 || etPhone.length() == 0
                        || etAddress.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "请填写完整注册信息", Toast.LENGTH_LONG).show();
                    return;
                }

                /*提交注册*/
                UsersEntity usersEntity = new UsersEntity();
                usersEntity.setuName(etName.getText().toString());
                usersEntity.setuPassword(etPwd.getText().toString());
                usersEntity.setuIdcard(etIdcard.getText().toString());
                usersEntity.setuPhone(etPhone.getText().toString());
                usersEntity.setuAddress(etAddress.getText().toString());

                String depart = spDepart.getSelectedItem().toString();
                int departId = -1;
                for(DepartmentEntity d : departList) {
                    if(depart == d.getdName()) {
                        departId = d.getdId(); break;
                    }
                }
                if(departId == -1) {
                    Toast.makeText(RegisterActivity.this, "下拉条获取出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                usersEntity.setdId(departId);

                String url = mApplication.USER_ADD;
                try {
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(JSON.toJSONString(usersEntity)),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //获取到刚注册用户
                                try {
                                    int responseCode = response.getInt("stateCode");
                                    if (responseCode == 404) { //出现错误
                                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (responseCode == 500) {
                                        String userJson = response.getString("entity");
                                        UsersEntity usersEntity = JSON.parseObject(userJson, UsersEntity.class);
                                        Intent intent = new Intent();
                                        intent.setClass(RegisterActivity.this, LoginActivity.class);
                                        intent.putExtra("uId", usersEntity.getuId());
                                        setResult(1, intent);
                                        Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UPDATE - ERROR", error.getMessage(), error);
                        byte[] htmlBodyBytes = error.networkResponse.data;
                        Log.e("UPDATE - ERROR", new String(htmlBodyBytes), error);
                        return;
                    }
                }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Accept", "application/json");
                            headers.put("Content-Type", "application/json; charset=UTF-8");
                            return headers;
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=UTF-8";
                        }
                    };
                    mRequestQueue.add(jsonRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化view和获取spinner内容
     */
    private void initView() {
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etName = (EditText) findViewById(R.id.et_nickname);
        etIdcard = (EditText) findViewById(R.id.et_idcard);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etAddress = (EditText) findViewById(R.id.et_address);
        spDepart = (Spinner) findViewById(R.id.sp_depart);
        btnOk = (Button) findViewById(R.id.bt_register_ok);

        /*初始化spinner*/
        String url = mApplication.DEPART_LIST_GET;
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(RegisterActivity.this, "获取部门列表失败", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(responseCode == 500) {
                                String listJson = response.getString("entity");
                                departList = (ArrayList)JSON.parseArray(listJson, DepartmentEntity.class);

                                ArrayList data = new ArrayList();
                                if(departList!=null) {
                                    for(DepartmentEntity d : departList) {
                                        data.add(d.getdName());
                                    }
                                }
                                //适配器
                                arr_adapter= new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, data);
                                //设置样式
                                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //加载适配器
                                spDepart.setAdapter(arr_adapter);

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("UPDATE - ERROR", error.getMessage(), error);
                byte[] htmlBodyBytes = error.networkResponse.data;
                Log.e("UPDATE - ERROR", new String(htmlBodyBytes), error);
                Toast.makeText(RegisterActivity.this, "获取部门列表失败", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        mRequestQueue.add(jsonRequest);

    }
}
