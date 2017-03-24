package com.example.barbara.newspapersub.Fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.barbara.newspapersub.Model.NewspaperclassEntity;
import com.example.barbara.newspapersub.Model.OrdersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计订购情况
 */
public class UserFragment2 extends ListFragment {
    private static final String TAG = "UserFragment2";

    private ListView list;
    private Spinner select;
    private SimpleAdapter simpleAdapter;

    private MyApplication myApplication;
    private RequestQueue requestQueue;
    ArrayList<Map<String, Object>> list1 = new ArrayList<>();
    ArrayList<OrdersEntity> ordersEntities;

    ArrayAdapter<String> arr_adapter;
    private ArrayList<NewspaperclassEntity> classList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApplication = (MyApplication)getActivity().getApplication();
        requestQueue = myApplication.getRequestQueue();

        initOrderList();

        simpleAdapter = new SimpleAdapter(getActivity(), list1, R.layout.item_order,
                new String[]{"oid", "nid", "copies", "month"},
                new int[]{R.id.item_oid, R.id.item_nid, R.id.item_copies, R.id.item_month});
        setListAdapter(simpleAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_2, container, false);

        list = (ListView) view.findViewById(android.R.id.list);
        select = (Spinner) view.findViewById(R.id.spinner_select);

        initSpinner();
        initListener();

        return view;
    }

    private void initSpinner() {
        /*初始化spinner*/
        String url = myApplication.CLASS_LIST_GET;
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
                                classList = (ArrayList) JSON.parseArray(listJson, NewspaperclassEntity.class);

                                ArrayList data = new ArrayList();
                                if(classList!=null) {
                                    for(NewspaperclassEntity d : classList) {
                                        data.add(d.getcName());
                                    }
                                }
                                //适配器
                                arr_adapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
                                //设置样式
                                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //加载适配器
                                select.setAdapter(arr_adapter);

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
                Toast.makeText(getActivity(), "获取类别列表失败", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        requestQueue.add(jsonRequest);
    }

    private void initListener() {

        /*设置监听*/
        select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String clsName = select.getSelectedItem().toString();
                NewspaperclassEntity cls = null;
                for (NewspaperclassEntity n : classList) { //找到被选中的类型实体
                    if (clsName.equals(n.getcName())) {
                        cls = n;
                        break;
                    }
                }
                //取出类型下的报刊列表
                if (cls == null) {
                    Log.e(TAG, "null point");
                    return;
                }
                ArrayList<NewspaperEntity> newspaperEntities = (ArrayList) cls.getNewspapersByCId();

                if(ordersEntities != null) {
                    list1.clear();
                    for (OrdersEntity o : ordersEntities) {
                        for (NewspaperEntity n : newspaperEntities) {
                            if (o.getnId() == n.getnId()) {
                                HashMap map = new HashMap();
                                map.put("oid", o.getoId());
                                map.put("nid", o.getnId());
                                map.put("copies", o.getoNum());
                                map.put("month", o.getoMonth());
                                list1.add(map);
                            }
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initOrderList() {
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
                                ordersEntities = (ArrayList) JSON.parseArray(newsList, OrdersEntity.class);
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
    }
}
