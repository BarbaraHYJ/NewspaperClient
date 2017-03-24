package com.example.barbara.newspapersub.Fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.barbara.newspapersub.Actitity.admin.AddUserActivity;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.UsersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息展示
 */
public class AdminFragment0 extends ListFragment {

    private ListView list;
    private SimpleAdapter simpleAdapter;
    private Button add;

    private MyApplication myApplication;
    private RequestQueue requestQueue;
    ArrayList<Map<String, Object>> list1 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_3, container, false);
        list = (ListView) view.findViewById(android.R.id.list);
        add = (Button)view.findViewById(R.id.addToList);
        add.setText("添加用户");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddUserActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication)getActivity().getApplication();
        requestQueue = myApplication.getRequestQueue();

        simpleAdapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_user,
                new String[]{"id", "name"},
                new int[]{R.id.item_id, R.id.item_name});
        setListAdapter(simpleAdapter);
    }


    private List<Map<String, Object>> getData() {
        String url = myApplication.USER_LIST_GET;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) { /*加入list*/
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(getActivity(), "获取用户列表失败", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(responseCode == 500) {
                                String newsList = response.getString("entity");
                                ArrayList<UsersEntity> usersEntities = (ArrayList) JSON.parseArray(newsList, UsersEntity.class);
                                if(usersEntities != null) {
                                    list1.clear();
                                    for(UsersEntity n : usersEntities) {
                                        HashMap map = new HashMap();
                                        map.put("id", n.getuId());
                                        map.put("name", n.getuName());
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
