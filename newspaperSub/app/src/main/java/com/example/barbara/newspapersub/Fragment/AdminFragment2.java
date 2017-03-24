package com.example.barbara.newspapersub.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.barbara.newspapersub.Actitity.admin.SelectByDepartActivity;
import com.example.barbara.newspapersub.Actitity.admin.SelectByNewsActivity;
import com.example.barbara.newspapersub.Actitity.admin.SelectByUserActivity;
import com.example.barbara.newspapersub.R;

/**
 * 查询页面
 */
public class AdminFragment2 extends Fragment implements View.OnClickListener{

    private Button btUser, btDepart, btNews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_2, container, false);
        btUser = (Button) view.findViewById(R.id.select_by_user); btUser.setOnClickListener(this);
        btDepart = (Button) view.findViewById(R.id.select_by_department); btDepart.setOnClickListener(this);
        btNews = (Button) view.findViewById(R.id.select_by_newspaper); btNews.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_by_user:
                Intent intent = new Intent(getActivity(), SelectByUserActivity.class);
                startActivity(intent);
                break;
            case R.id.select_by_department:
                Intent intent1 = new Intent(getActivity(), SelectByDepartActivity.class);
                startActivity(intent1);
                break;
            case R.id.select_by_newspaper:
                Intent intent2 = new Intent(getActivity(), SelectByNewsActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
