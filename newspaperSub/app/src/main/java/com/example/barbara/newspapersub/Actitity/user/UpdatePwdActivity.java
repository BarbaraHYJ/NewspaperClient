package com.example.barbara.newspapersub.Actitity.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.barbara.newspapersub.Actitity.BaseActivity;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.UsersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码
 */
public class UpdatePwdActivity extends BaseActivity {

    private EditText etPwd, etAgain;
    private Button btOk;

    private MyApplication myApplication;
    private RequestQueue requestQueue;
    private UsersEntity loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        initView();

        myApplication = (MyApplication)getApplication();
        requestQueue = myApplication.getRequestQueue();
        loginUser = myApplication.getLoginUser();
    }

    private void initView() {
        etPwd = (EditText) findViewById(R.id.update_pwd);
        etAgain = (EditText) findViewById(R.id.update_again);
        btOk = (Button) findViewById(R.id.pwd_update_ok);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPwd.length() == 0 || etAgain.length() == 0) {
                    Toast.makeText(UpdatePwdActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                String pwd = etPwd.getText().toString();
                if (!pwd.equals(etAgain.getText().toString())) {
                    Toast.makeText(UpdatePwdActivity.this, "两次输入不一致", Toast.LENGTH_LONG).show();
                    return;
                }
                loginUser.setuPassword(pwd);

                try{
                    String s = JSON.toJSONString(loginUser);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, myApplication.USER_UPDATE, new JSONObject(s),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(UpdatePwdActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("UPDATE - ERROR", error.getMessage(), error);
                            byte[] htmlBodyBytes = error.networkResponse.data;
                            Log.e("UPDATE - ERROR", new String(htmlBodyBytes), error);
                            Toast.makeText(UpdatePwdActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
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
                requestQueue.add(jsonObjectRequest);

                } catch (JSONException ignored) {}

            }
        });
    }
}
