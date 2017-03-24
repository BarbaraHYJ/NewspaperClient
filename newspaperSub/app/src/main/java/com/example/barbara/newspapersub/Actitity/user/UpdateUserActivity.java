package com.example.barbara.newspapersub.Actitity.user;

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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.barbara.newspapersub.Actitity.BaseActivity;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.DepartmentEntity;
import com.example.barbara.newspapersub.Model.UsersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改用户资料Activity
 */
public class UpdateUserActivity extends BaseActivity {

    private EditText etName, etIdcard, etPhone, etAddress;
    private Spinner spDepart;
    private Button btnOk;

    private MyApplication mApplication;
    private UsersEntity loginUser;
    private ArrayList<DepartmentEntity> departList;

    ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        mApplication = (MyApplication)getApplication();
        loginUser = mApplication.getLoginUser();
        departList = mApplication.getDepartList();

        initView();

    }


    private void initView() {
        etName = (EditText) findViewById(R.id.et_update_name);
        etIdcard = (EditText) findViewById(R.id.et_update_idcard);
        etPhone = (EditText) findViewById(R.id.et_update_phone);
        etAddress = (EditText) findViewById(R.id.et_update_address);
        spDepart = (Spinner) findViewById(R.id.sp_update_depart);
        btnOk = (Button) findViewById(R.id.bt_update_ok);

        etName.setText(loginUser.getuName());
        etIdcard.setText(loginUser.getuIdcard());
        etPhone.setText(loginUser.getuPhone());
        etAddress.setText(loginUser.getuAddress());
        /*初始化下拉菜单的内容*/
        ArrayList data = new ArrayList();
        for(DepartmentEntity d : departList) {
            data.add(d.getdName());
        }
        //适配器
        arr_adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spDepart.setAdapter(arr_adapter);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.length() == 0 || etIdcard.length() == 0 || etPhone.length() == 0 ||
                        etAddress.length() == 0) {
                    Toast.makeText(UpdateUserActivity.this, "请不要留空", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser.setuName(etName.getText().toString());
                loginUser.setuIdcard(etIdcard.getText().toString());
                loginUser.setuPhone(etPhone.getText().toString());
                loginUser.setuAddress(etAddress.getText().toString());
                String depart = spDepart.getSelectedItem().toString();
                int departId = -1;
                for(DepartmentEntity d : departList) {
                    if(depart == d.getdName()) {
                        departId = d.getdId(); break;
                    }
                }
                if(departId == -1) {
                    Toast.makeText(UpdateUserActivity.this, "下拉条获取出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser.setdId(departId);
                mApplication.userLogin(loginUser);
                /**提交到服务器**/
                try {
                    String s = JSON.toJSONString(loginUser);
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, mApplication.USER_UPDATE, new JSONObject(s),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(UpdateUserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("UPDATE - ERROR", error.getMessage(), error);
                            byte[] htmlBodyBytes = error.networkResponse.data;
                            Log.e("UPDATE - ERROR", new String(htmlBodyBytes), error);
                            Toast.makeText(UpdateUserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
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
                    mApplication.getRequestQueue().add(jsonRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });
    }
}
