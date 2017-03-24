package com.example.barbara.newspapersub.Actitity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.barbara.newspapersub.Actitity.admin.AdminActivity;
import com.example.barbara.newspapersub.Actitity.user.RegisterActivity;
import com.example.barbara.newspapersub.Actitity.user.UserActivity;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.AdminuserEntity;
import com.example.barbara.newspapersub.Model.UsersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int REGISTER_CODE = 101;
    private static final String TAG = "LoginActivity";

    private MyApplication mApplication;
    private EditText et_name, et_pwd;
    private Button btnLogin, btnRegister, btnNameClear, btnPwdClear, btnEye;
    private TextWatcher nameWatcher, pwdWatcher;
    private RadioGroup radioGroup;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mApplication = (MyApplication) getApplication();

        mRequestQueue = mApplication.getRequestQueue();

        initWatcher(); //初始化TextWatcher
        initView(); //findViewById……方法,绑定监听器



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login: /*登录*/
                login();
                break;
            case R.id.register: /*注册*/
                register();
                break;
            case R.id.bt_pwd_clear: /*清空密码*/
                et_pwd.setText("");
                break;
            case R.id.bt_username_clear: /*清空用户名*/
                et_name.setText("");
                et_pwd.setText("");
                break;
            case R.id.bt_pwd_eye: /*显示、隐藏密码*/
                if(et_pwd.getInputType() == (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else{
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {

        /*检查输入内容是否为空*/
        if(et_name.length() == 0 || et_pwd.length() == 0 ) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        /*检查输入合法性并发起请求*/
        switch (radioGroup.getCheckedRadioButtonId()) {
            /*普通用户登录*/
            case R.id.radioUser: {
                //检查是不是为纯数字
                String name = et_name.getText().toString();
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(name);
                if (!m.matches()) {
                    Toast.makeText(this, "普通用户名为纯数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                //发起请求
                String url = mApplication.USER_LOGIN + "/" + name + "/" + et_pwd.getText().toString();
                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int responseCode = response.getInt("stateCode");
                                    if (responseCode == 404) { //出现错误
                                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (responseCode == 500) {
                                        String userJson = response.getString("entity");
                                        UsersEntity usersEntity = JSON.parseObject(userJson, UsersEntity.class);
                                        mApplication.userLogin(usersEntity); //更新登录用户信息
                                        //跳转到用户页面
                                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                                        startActivity(intent);
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
                };
                mRequestQueue.add(jsonRequest);
                break;
            }

            /*管理员登录*/
            case R.id.radioAdmin:
                String url = mApplication.ADMIN_LOGIN+ "/" + et_name.getText().toString() + "/" +et_pwd.getText().toString();
                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int responseCode = response.getInt("stateCode");
                                    if (responseCode == 404) { //出现错误
                                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (responseCode == 500) {
                                        String adminJson = response.getString("entity");
                                        AdminuserEntity adminuserEntity = JSON.parseObject(adminJson, AdminuserEntity.class);
                                        mApplication.adminLogin(adminuserEntity); //更新登录管理员信息
                                        //跳转到管理员页面
                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                        startActivity(intent);
                                    }
                                } catch (JSONException ignored) {
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
                });
                mRequestQueue.add(jsonRequest);
                break;
        }
    }

    /**
     * 用户注册
     */
    private void register() {
        /*跳转注册页面，带返回值*/
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTER_CODE) {
            if(resultCode == 1) {
                int uId = data.getIntExtra("uId", 0);
                et_name.setText(String.valueOf(uId));
                et_pwd.setText("");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initWatcher() {
        nameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                et_pwd.setText("");
                if(s.toString().length() > 0) {
                    btnNameClear.setVisibility(View.VISIBLE);
                }else {
                    btnNameClear.setVisibility(View.INVISIBLE);
                }
            }
        };

        pwdWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0) {
                    btnPwdClear.setVisibility(View.VISIBLE);
                }else {
                    btnPwdClear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.username);
        et_pwd = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login);
        btnRegister = (Button) findViewById(R.id.register);
        btnNameClear = (Button) findViewById(R.id.bt_username_clear);
        btnPwdClear = (Button) findViewById(R.id.bt_pwd_clear);
        btnEye = (Button) findViewById(R.id.bt_pwd_eye);
        radioGroup = (RadioGroup) findViewById(R.id.radio);


        btnRegister.setOnClickListener(this);
        btnNameClear.setOnClickListener(this);
        btnPwdClear.setOnClickListener(this);
        btnEye.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        et_name.addTextChangedListener(nameWatcher);
        et_pwd.addTextChangedListener(pwdWatcher);
    }
}
