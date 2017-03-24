package com.example.barbara.newspapersub.Actitity.admin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.barbara.newspapersub.Actitity.BaseActivity;
import com.example.barbara.newspapersub.Contents.MyApplication;
import com.example.barbara.newspapersub.Fragment.AdminFragment0;
import com.example.barbara.newspapersub.Fragment.AdminFragment1;
import com.example.barbara.newspapersub.Fragment.AdminFragment2;
import com.example.barbara.newspapersub.Fragment.AdminFragment3;
import com.example.barbara.newspapersub.Model.AdminuserEntity;
import com.example.barbara.newspapersub.R;

import java.util.ArrayList;

/**
 * Created by barbara on 2016/12/26.
 */
public class AdminActivity extends BaseActivity implements View.OnClickListener {

    private RadioButton navButton0, navButton1, navButton2, navButton3;
    private ArrayList<RadioButton> navButtons;
    private ArrayList<Fragment> fragments;

    private AdminuserEntity loginAdmin;
    private MyApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //获取登录管理员
        mApplication = (MyApplication) getApplication();
        loginAdmin = mApplication.getLoginAdmin();

        //初始化组件
        initView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_button0:
                displayFragment(0);
                break;
            case R.id.nav_button1:
                displayFragment(1);
                break;
            case R.id.nav_button2:
                displayFragment(2);
                break;
            case R.id.nav_button3:
                displayFragment(3);
                break;
        }

    }

    private void displayFragment(int position) {

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, fragments.get(position)).commit();

    }

    private void initView() {
        navButtons = new ArrayList<>();
        navButton0 = (RadioButton) findViewById(R.id.nav_button0); navButtons.add(navButton0);
        navButton0.setOnClickListener(this); navButton0.setButtonDrawable(R.drawable.peoples32);
        navButton1 = (RadioButton) findViewById(R.id.nav_button1); navButtons.add(navButton1);
        navButton1.setOnClickListener(this); navButton1.setButtonDrawable(R.drawable.newspaper48);
        navButton2 = (RadioButton) findViewById(R.id.nav_button2); navButtons.add(navButton2);
        navButton2.setOnClickListener(this); navButton2.setButtonDrawable(R.drawable.search48);
        navButton3 = (RadioButton) findViewById(R.id.nav_button3); navButtons.add(navButton3);
        navButton3.setOnClickListener(this); navButton3.setButtonDrawable(R.drawable.calculator48);

        fragments = new ArrayList<>();
        fragments.add(new AdminFragment0());
        fragments.add(new AdminFragment1());
        fragments.add(new AdminFragment2());
        fragments.add(new AdminFragment3());

        //默认激活
        navButton0.setChecked(true);
        displayFragment(0);
    }
}
