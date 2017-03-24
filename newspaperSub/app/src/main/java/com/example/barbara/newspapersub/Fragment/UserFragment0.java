package com.example.barbara.newspapersub.Fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.barbara.newspapersub.Actitity.user.BuyActivity;
import com.example.barbara.newspapersub.Adapter.ListClickAdapter;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.NewspaperEntity;
import com.example.barbara.newspapersub.R;
import com.example.barbara.newspapersub.Util.Arith;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 新闻信息订购
 */
public class UserFragment0 extends ListFragment {

    private ListView list;
    private SimpleAdapter simpleAdapter;
    private Button button;
    ListClickAdapter listClickAdapter;

    private MyApplication myApplication;
    private RequestQueue requestQueue;
    ArrayList<Map<String, Object>> list1 = new ArrayList<>();

    private ArrayList<Integer> buyList;
    private double money;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApplication = (MyApplication)getActivity().getApplication();
        requestQueue = myApplication.getRequestQueue();

        buyList = new ArrayList<>();
        money = 0.0;

//        simpleAdapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_buynews,
//                new String[]{"id", "name", "office", "price", "cycle", "content"},
//                new int[]{R.id.item_nid, R.id.item_name, R.id.item_office, R.id.item_price, R.id.item_cycle, R.id.item_content});
//        setListAdapter(simpleAdapter);

        listClickAdapter = new ListClickAdapter(getActivity(), getData());
        setListAdapter(listClickAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_3, container, false);
        list = (ListView) view.findViewById(android.R.id.list);
        button = (Button)view.findViewById(R.id.addToList);
        button.setText("订购报刊");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buyList.size() == 0) {
                    Toast.makeText(getActivity(), "请先选择报刊", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), BuyActivity.class);
                intent.putExtra("list", buyList);
                startActivity(intent);
            }
        });
        return view;
    }

    private ArrayList<Map<String, Object>> getData() {
        String url = myApplication.NEWS_LIST_GET;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) { /*加入list*/
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(getActivity(), "获取报刊列表失败", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(responseCode == 500) {
                                String newsList = response.getString("entity");
                                ArrayList<NewspaperEntity> newspaperEntities = (ArrayList) JSON.parseArray(newsList, NewspaperEntity.class);
                                if(newspaperEntities != null) {
                                    list1.clear();
                                    for(NewspaperEntity n : newspaperEntities) {
                                        HashMap map = new HashMap();
                                        map.put("id", n.getnId());
                                        map.put("name", n.getnName());
                                        map.put("office", n.getnOffice());
                                        map.put("price", n.getnPrice());
                                        map.put("cycle", n.getnTime());
                                        map.put("content", n.getnContent());
                                        map.put("isClick", false);
                                        list1.add(map);
                                    }
                                }
                                listClickAdapter.notifyDataSetChanged();
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TextView etId, etPrice;
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.check_buy);
        etId = (TextView) v.findViewById(R.id.item_nid);
        etPrice = (TextView) v.findViewById(R.id.item_price);

        if(checkBox.isChecked()) { //选中了
            checkBox.setChecked(false); //变成不选
            money = Arith.sub(money, Double.parseDouble(etPrice.getText().toString())); //减价格
            buyList.remove((Integer)Integer.parseInt(etId.getText().toString())); //减去购买订单
            list1.get(position).put("isClick", false);
            listClickAdapter.notifyDataSetChanged();
        }else { //未选中
            checkBox.setChecked(true); //变成选
            money = Arith.add(money, Double.parseDouble(etPrice.getText().toString()));
            buyList.add(Integer.parseInt(etId.getText().toString()));
            list1.get(position).put("isClick", true);
            listClickAdapter.notifyDataSetChanged();
        }

        if(buyList.size() != 0) button.setText("已选"+buyList.size()+"报刊共计：$"+money);
        else button.setText("订阅报刊");
    }
}
