package com.example.barbara.newspapersub.Actitity.admin;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.NewspaperEntity;
import com.example.barbara.newspapersub.Model.OrdersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 由报刊查询订单
 */
public class SelectByNewsActivity extends ListActivity{
    private static final String TAG = "SelectByNewsActivity";

    private ListView listView;
    private Spinner spinner;
    private SimpleAdapter simpleAdapter;

    private MyApplication myApplication;
    private RequestQueue requestQueue;
    ArrayList<Map<String, Object>> list1 = new ArrayList<>();
    ArrayList<OrdersEntity> ordersEntities;

    ArrayAdapter<String> arr_adapter;
    ArrayList<NewspaperEntity> newspaperEntities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_2);

        myApplication = (MyApplication)getApplication();
        requestQueue = myApplication.getRequestQueue();

        initView();
        initListener();


        simpleAdapter = new SimpleAdapter(this, list1, R.layout.item_order,
                new String[]{"oid", "nid", "copies", "month"},
                new int[]{R.id.item_oid, R.id.item_nid, R.id.item_copies, R.id.item_month});
        setListAdapter(simpleAdapter);
    }

    private void initListener() {
        //设置监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*清除*/
                list1.clear();
                simpleAdapter.notifyDataSetChanged();

                String newsName = spinner.getSelectedItem().toString();
                int nId = -1;
                if (newspaperEntities == null) return;
                for (NewspaperEntity n : newspaperEntities) {
                    if (newsName.equals(n.getnName())) {
                        nId = n.getnId();
                        break;
                    }
                }
                if (nId == -1) {
                    Log.e(TAG, "null point");
                    return;
                }

                Log.e(TAG, String.valueOf(nId));

                String url = myApplication.ORDER_LIST_GET_BY_NEWS + "/" + nId;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) { /*加入list*/
                                try {
                                    int responseCode = response.getInt("stateCode");
                                    if (responseCode == 404) {
                                        Toast.makeText(SelectByNewsActivity.this, "获取订单列表失败", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (responseCode == 500) {
                                        String newsList = response.getString("entity");
                                        ordersEntities = (ArrayList) JSON.parseArray(newsList, OrdersEntity.class);
                                        if(ordersEntities == null) return;
                                        list1.clear();
                                        for(OrdersEntity o : ordersEntities) {
                                            HashMap map = new HashMap();
                                            map.put("oid", o.getoId());
                                            map.put("nid", o.getnId());
                                            map.put("copies", o.getoNum());
                                            map.put("month", o.getoMonth());
                                            list1.add(map);
                                        }
                                        simpleAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonObjectRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initView() {
        listView = (ListView) findViewById(android.R.id.list);
        spinner = (Spinner) findViewById(R.id.spinner_select);

         /*初始化spinner*/
        String url = myApplication.NEWS_LIST_GET;
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(SelectByNewsActivity.this, "获取报刊列表失败", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(responseCode == 500) {
                                String listJson = response.getString("entity");
                                newspaperEntities = (ArrayList) JSON.parseArray(listJson, NewspaperEntity.class);

                                ArrayList data = new ArrayList();
                                if(newspaperEntities!=null) {
                                    for(NewspaperEntity n : newspaperEntities) {
                                        data.add(n.getnName());
                                    }
                                }
                                //适配器
                                arr_adapter= new ArrayAdapter<>(SelectByNewsActivity.this, android.R.layout.simple_spinner_item, data);
                                //设置样式
                                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //加载适配器
                                spinner.setAdapter(arr_adapter);

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
                Toast.makeText(SelectByNewsActivity.this, "获取报刊列表失败", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        requestQueue.add(jsonRequest);
    }

}
