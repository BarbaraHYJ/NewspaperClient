package com.example.barbara.newspapersub.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Model.DepartmentEntity;
import com.example.barbara.newspapersub.Model.NewspaperEntity;
import com.example.barbara.newspapersub.Model.UsersEntity;
import com.example.barbara.newspapersub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 统计功能页面
 */
public class AdminFragment3 extends Fragment implements AdapterView.OnItemSelectedListener{
    private String TAG = "AdminFragment3";

    private Spinner spUser, spDepart, spNews;
    private TextView tvUserCopies, tvUserMonths, tvDepartCopies, tvDepartMonths, tvNewsCopies, tvNewsMonths;

    private MyApplication myApplication;
    private RequestQueue requestQueue;

    private ArrayList<UsersEntity> usersEntities;
    private ArrayList<DepartmentEntity> departmentEntities;
    private ArrayList<NewspaperEntity> newspaperEntities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApplication = (MyApplication) getActivity().getApplication();
        requestQueue = myApplication.getRequestQueue();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_calculation, container, false);

        spUser = (Spinner) view.findViewById(R.id.spinner_user); spUser.setOnItemSelectedListener(this);
        spDepart = (Spinner) view.findViewById(R.id.spinner_depart); spDepart.setOnItemSelectedListener(this);
        spNews = (Spinner) view.findViewById(R.id.spinner_news); spNews.setOnItemSelectedListener(this);
        tvUserCopies = (TextView) view.findViewById(R.id.tv_user_copies);
        tvUserMonths = (TextView) view.findViewById(R.id.tv_user_months);
        tvDepartCopies = (TextView) view.findViewById(R.id.tv_depart_copies);
        tvDepartMonths = (TextView) view.findViewById(R.id.tv_depart_months);
        tvNewsCopies = (TextView) view.findViewById(R.id.tv_news_copies);
        tvNewsMonths = (TextView) view.findViewById(R.id.tv_news_months);
        initSpinner();

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_user: {
                Object item = spUser.getSelectedItem();
                if(item == null) return;
                String userName = item.toString();
                if (usersEntities == null) {
                    Log.e(TAG, "null point");
                    return;
                }
                int uId = -1;
                for (UsersEntity u : usersEntities) {
                    if (userName.equals(u.getuName())) {
                        uId = u.getuId();
                    }
                }

                if (uId == -1) return;
                String userUrl = myApplication.ORDER_SUM_BY_USER + "/" + uId;
                JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET, userUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int responseCode = response.getInt("stateCode");
                                    if (responseCode == 404) {
                                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                                        tvUserCopies.setText(String.valueOf(0));
                                        tvUserMonths.setText(String.valueOf(0));
                                        return;
                                    } else if (responseCode == 500) {
                                        JSONObject sum = response.getJSONObject("entity");
                                        int userCopies = sum.getInt("copies");
                                        int userMonths = sum.getInt("months");
                                        tvUserCopies.setText(String.valueOf(userCopies));
                                        tvUserMonths.setText(String.valueOf(userMonths));
                                    }
                                } catch (JSONException ignored) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                requestQueue.add(userRequest);
                break;
            }
            case R.id.spinner_depart: {
                Object item = spDepart.getSelectedItem();
                if(item == null) return;
                String departName = item.toString();
                if (departmentEntities == null) {
                    Log.e(TAG, "null point");
                    return;
                }
                int dId = -1;
                for (DepartmentEntity d : departmentEntities) {
                    if (departName.equals(d.getdName())) {
                        dId = d.getdId();
                    }
                }

                if (dId == -1) return;
                String departUrl = myApplication.ORDER_SUM_BY_DEPART + "/" + dId;
                JsonObjectRequest departRequest = new JsonObjectRequest(Request.Method.GET, departUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int responseCode = response.getInt("stateCode");
                                    if (responseCode == 404) {
                                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                                        tvDepartCopies.setText(String.valueOf(0));
                                        tvDepartMonths.setText(String.valueOf(0));
                                        return;
                                    } else if (responseCode == 500) {
                                        JSONObject sum = response.getJSONObject("entity");
                                        int departCopies = sum.getInt("copies");
                                        int departMonths = sum.getInt("months");
                                        tvDepartCopies.setText(String.valueOf(departCopies));
                                        tvDepartMonths.setText(String.valueOf(departMonths));
                                    }
                                } catch (JSONException ignored) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                requestQueue.add(departRequest);
                break;
            }
            case R.id.spinner_news: {
//                Log.e(TAG, "spinner news selected");
                Object item = spNews.getSelectedItem();
                if(item == null) return;
                String newsName = item.toString();
                if (newspaperEntities == null) {
                    Log.e(TAG, "null point");
                    return;
                }
                int nId = -1;
                for (NewspaperEntity n : newspaperEntities) {
                    if (newsName.equals(n.getnName())) {
                        nId = n.getnId();
                    }
                }

                if (nId == -1) return;
                String newsUrl = myApplication.ORDER_SUM_BY_NEWS + "/" + nId;
                JsonObjectRequest newsRequest = new JsonObjectRequest(Request.Method.GET, newsUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int responseCode = response.getInt("stateCode");
                                    if (responseCode == 404) {
                                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                                        tvNewsCopies.setText(String.valueOf(0));
                                        tvNewsMonths.setText(String.valueOf(0));
                                        return;
                                    } else if (responseCode == 500) {
                                        JSONObject sum = response.getJSONObject("entity");
                                        int newsCopies = sum.getInt("copies");
                                        int newsMonths = sum.getInt("months");
                                        tvNewsCopies.setText(String.valueOf(newsCopies));
                                        tvNewsMonths.setText(String.valueOf(newsMonths));
                                    }
                                } catch (JSONException ignored) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                requestQueue.add(newsRequest);
                break;
            }
            default:
                Log.e(TAG, String.valueOf(parent.getId()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initSpinner() {
        /*init spinner user*/
        String url = myApplication.USER_LIST_GET;
        final JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(getActivity(), "获取用户列表失败", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(responseCode == 500) {
                                String listJson = response.getString("entity");
                                usersEntities = (ArrayList) JSON.parseArray(listJson, UsersEntity.class);

                                ArrayList data = new ArrayList();
                                if(usersEntities!=null) {
                                    for(UsersEntity u : usersEntities) {
                                        data.add(u.getuName());
                                    }
                                }
                                //适配器
                                ArrayAdapter<String> user_adapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
                                //设置样式
                                user_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //加载适配器
                                spUser.setAdapter(user_adapter);

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
        requestQueue.add(userRequest);

        /*init depart spinner*/
        String url1 = myApplication.DEPART_LIST_GET;
        JsonRequest<JSONObject> departRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
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
                                departmentEntities = (ArrayList) JSON.parseArray(listJson, DepartmentEntity.class);

                                ArrayList data = new ArrayList();
                                if(departmentEntities!=null) {
                                    for(DepartmentEntity d : departmentEntities) {
                                        data.add(d.getdName());
                                    }
                                }
                                //适配器
                                ArrayAdapter<String> depart_adapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
                                //设置样式
                                depart_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //加载适配器
                                spDepart.setAdapter(depart_adapter);

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
                Toast.makeText(getActivity(), "获取部门列表失败", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        requestQueue.add(departRequest);

        /*init news spinner*/
        String url2 = myApplication.NEWS_LIST_GET;
        JsonRequest<JSONObject> newsRequest = new JsonObjectRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("stateCode");
                            if(responseCode == 404) {
                                Toast.makeText(getActivity(), "获取报刊列表失败", Toast.LENGTH_SHORT).show();
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
                                ArrayAdapter<String> news_adapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
                                //设置样式
                                news_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //加载适配器
                                spNews.setAdapter(news_adapter);

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
                Toast.makeText(getActivity(), "获取报刊列表失败", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        requestQueue.add(newsRequest);
    }
}
