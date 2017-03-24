package com.example.barbara.newspapersub.Actitity.user;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.barbara.newspapersub.Adapter.ListEditorAdapter;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.OrdersEntity;
import com.example.barbara.newspapersub.R;
import com.example.barbara.newspapersub.Util.Arith;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 下单
 */
public class BuyActivity extends ListActivity {

    private ArrayList<Integer> buyList;

    private Button btOk;
    private ListView listView;
    private ListEditorAdapter listEditorAdapter;

    private MyApplication myApplication;
    private RequestQueue requestQueue;
    ArrayList<Map<String, Object>> list1 = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        Intent intent = getIntent();
        buyList = intent.getExtras().getIntegerArrayList("list");

        myApplication = (MyApplication) getApplication();
        requestQueue = myApplication.getRequestQueue();

        listView = (ListView) findViewById(android.R.id.list);
        btOk = (Button) findViewById(R.id.bt_create_order);

        listEditorAdapter = new ListEditorAdapter(this, getData());
        setListAdapter(listEditorAdapter);

        createOrder();
    }

    private void createOrder() {
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*检查输入*/
                for(int i = 0; i < buyList.size(); i++) {
                    if(list1.get(i).get("copies") == null || list1.get(i).get("months") == null ||
                            list1.get(i).get("copies").equals("")|| list1.get(i).get("months").equals("")) {
                        Toast.makeText(BuyActivity.this, "请输入完整信息", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                /*循环创建订单*/
                for(int i = 0; i < buyList.size(); i++) {
                    OrdersEntity ordersEntity = new OrdersEntity();
                    ordersEntity.setuId(myApplication.getLoginUser().getuId());
                    ordersEntity.setnId(Integer.parseInt(list1.get(i).get("news_id").toString()));
                    ordersEntity.setoMonth(Integer.parseInt(list1.get(i).get("months").toString()));
                    ordersEntity.setoNum(Integer.parseInt(list1.get(i).get("copies").toString()));
                    ordersEntity.setoId(Arith.getRandom4());

                    String url = myApplication.ORDER_ADD;
                    try {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(JSON.toJSONString(ordersEntity)),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

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
                        requestQueue.add(jsonObjectRequest);
                    }catch (JSONException ignored) {

                    }
                }
                Toast.makeText(BuyActivity.this, "购买成功", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }





    private ArrayList<Map<String, Object>> getData() {

        for(Integer i : buyList) {
            Map map = new HashMap();
            map.put("news_id", i);
            map.put("copies", null);
            map.put("months", null);
            list1.add(map);
        }
        return list1;
    }
}
