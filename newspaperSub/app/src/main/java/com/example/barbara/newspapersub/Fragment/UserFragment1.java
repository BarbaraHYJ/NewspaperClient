package com.example.barbara.newspapersub.Fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.OrdersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单信息展示
 */
public class UserFragment1 extends ListFragment {

    private ListView list;
    private SimpleAdapter simpleAdapter;
    private TextView copies, months;

    private MyApplication myApplication;
    private RequestQueue requestQueue;
    ArrayList<Map<String, Object>> list1 = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApplication = (MyApplication)getActivity().getApplication();
        requestQueue = myApplication.getRequestQueue();

        simpleAdapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_order,
                new String[]{"oid", "nid", "copies", "month"},
                new int[]{R.id.item_oid, R.id.item_nid, R.id.item_copies, R.id.item_month});
        setListAdapter(simpleAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_1, container, false);
        list = (ListView) view.findViewById(android.R.id.list);
        copies = (TextView) view.findViewById(R.id.tv_copies);
        months = (TextView) view.findViewById(R.id.tv_months);
        initTv();
        return view;
    }

    private void initTv() {
        String uid = String.valueOf(myApplication.getLoginUser().getuId());
        String url = myApplication.ORDER_SUM_BY_USER + "/" + uid;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                                copies.setText(String.valueOf(0));
                                months.setText(String.valueOf(0));
                                return;
                            }else if(responseCode == 500) {
                                JSONObject sum = response.getJSONObject("entity");
                                int copies1 = sum.getInt("copies");
                                int months1 = sum.getInt("months");
                                copies.setText(String.valueOf(copies1));
                                months.setText(String.valueOf(months1));
                            }
                        }catch (JSONException e) {}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonObjectRequest);
    }


    private ArrayList<Map<String, Object>> getData() {
        String uid = String.valueOf(myApplication.getLoginUser().getuId());
        String url = myApplication.ORDER_LIST_GET_BY_USER + "/" + uid;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) { /*加入list*/
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(getActivity(), "获取订单列表失败", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(responseCode == 500) {
                                String newsList = response.getString("entity");
                                ArrayList<OrdersEntity> ordersEntities = (ArrayList) JSON.parseArray(newsList, OrdersEntity.class);
                                if(ordersEntities != null) {
                                    list1.clear();
                                    for(OrdersEntity n : ordersEntities) {
                                        HashMap map = new HashMap();
                                        map.put("oid", n.getoId());
                                        map.put("nid", n.getnId());
                                        map.put("copies", n.getoNum());
                                        map.put("month", n.getoMonth());
                                        list1.add(map);
                                    }
                                }
                                simpleAdapter.notifyDataSetChanged();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

        return list1;
    }
}
