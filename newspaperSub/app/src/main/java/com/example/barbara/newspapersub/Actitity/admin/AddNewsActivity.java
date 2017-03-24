package com.example.barbara.newspapersub.Actitity.admin;

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
import com.example.barbara.newspapersub.Actitity.BaseActivity;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.NewspaperEntity;
import com.example.barbara.newspapersub.Model.NewspaperclassEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by barbara on 2016/12/28.
 */
public class AddNewsActivity extends BaseActivity {

    private ArrayList<NewspaperclassEntity> classList;

    private RequestQueue mRequestQueue;
    private MyApplication mApplication;
    private EditText etId, etName, etOffice, etCycle, etPrice, etContent;
    private Spinner spClass;
    private Button btnOk;

    ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        mApplication = (MyApplication)getApplication();
        mRequestQueue = mApplication.getRequestQueue();

        initView();
        initListener();
    }


    private void initListener() {

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*内容检查*/
                if (etName.length() == 0 || etId.length() == 0 || etOffice.length() == 0 || etCycle.length() == 0
                        || etPrice.length() == 0 || etContent.length() == 0) {
                    Toast.makeText(AddNewsActivity.this, "请填写完整注册信息", Toast.LENGTH_LONG).show();
                    return;
                }

                /*提交添加*/
                NewspaperEntity newspaperEntity = new NewspaperEntity();
                newspaperEntity.setnId(Integer.parseInt(etId.getText().toString()));
                newspaperEntity.setnName(etName.getText().toString());
                newspaperEntity.setnOffice(etOffice.getText().toString());
                newspaperEntity.setnTime(Integer.parseInt(etCycle.getText().toString()));
                newspaperEntity.setnPrice(Double.parseDouble(etPrice.getText().toString()));
                newspaperEntity.setnContent(etContent.getText().toString());

                String cls = spClass.getSelectedItem().toString();
                int clsId = -1;
                for(NewspaperclassEntity n : classList) {
                    if(cls.equals(n.getcName())) {
                        clsId = n.getcId();
                        break;
                    }
                }
                if (clsId == -1) {
                    Toast.makeText(AddNewsActivity.this, "下拉条获取出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                newspaperEntity.setcId(clsId);

                /*网络提交*/
                String url = mApplication.NEWS_ADD;
                try {
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(JSON.toJSONString(newspaperEntity)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //获取到刚注册用户
                                    try {
                                        int responseCode = response.getInt("stateCode");
                                        if (responseCode == 404) { //出现错误
                                            Toast.makeText(AddNewsActivity.this, "增加失败", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else if (responseCode == 500) {
                                            Toast.makeText(AddNewsActivity.this, "增加成功", Toast.LENGTH_SHORT).show();
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




    private void initView() {
        etId = (EditText) findViewById(R.id.et_id);
        etName = (EditText) findViewById(R.id.et_name);
        etOffice = (EditText) findViewById(R.id.et_office);
        etCycle = (EditText) findViewById(R.id.et_cycle);
        etPrice = (EditText) findViewById(R.id.et_price);
        etContent = (EditText) findViewById(R.id.et_content);
        spClass = (Spinner) findViewById(R.id.sp_class);
        btnOk = (Button) findViewById(R.id.bt_add_news_ok);

        /*初始化spinner*/
        String url = mApplication.CLASS_LIST_GET;
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(AddNewsActivity.this, "获取类别列表失败", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(responseCode == 500) {
                                String listJson = response.getString("entity");
                                classList = (ArrayList) JSON.parseArray(listJson, NewspaperclassEntity.class);

                                ArrayList data = new ArrayList();
                                if(classList!=null) {
                                    for(NewspaperclassEntity d : classList) {
                                        data.add(d.getcName());
                                    }
                                }
                                //适配器
                                arr_adapter= new ArrayAdapter<>(AddNewsActivity.this, android.R.layout.simple_spinner_item, data);
                                //设置样式
                                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //加载适配器
                                spClass.setAdapter(arr_adapter);
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
                Toast.makeText(AddNewsActivity.this, "获取类别列表失败", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        mRequestQueue.add(jsonRequest);

    }

}

