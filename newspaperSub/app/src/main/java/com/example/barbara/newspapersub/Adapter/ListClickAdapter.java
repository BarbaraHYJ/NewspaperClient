package com.example.barbara.newspapersub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.barbara.newspapersub.R;

import java.util.List;
import java.util.Map;

/**
 * Created by barbara on 2017/1/3.
 */
public class ListClickAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData;// 存储的EditText值

    public ListClickAdapter(Context context, List<Map<String, Object>> data) {
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private Integer index = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_buynews, null);

        TextView id = (TextView)view.findViewById(R.id.item_nid);
        TextView name = (TextView)view.findViewById(R.id.item_name);
        TextView office = (TextView)view.findViewById(R.id.item_office);
        TextView price = (TextView)view.findViewById(R.id.item_price);
        TextView cycle = (TextView)view.findViewById(R.id.item_cycle);
        TextView content = (TextView)view.findViewById(R.id.item_content);
        CheckBox buy = (CheckBox)view.findViewById(R.id.check_buy);

        id.setText(String.valueOf(mData.get(position).get("id")));
        name.setText(String.valueOf(mData.get(position).get("name")));
        office.setText(String.valueOf(mData.get(position).get("office")));
        price.setText(String.valueOf(mData.get(position).get("price")));
        cycle.setText(String.valueOf(mData.get(position).get("cycle")));
        content.setText(String.valueOf(mData.get(position).get("content")));
        if(mData.get(position).get("isClick") == true) {
            buy.setChecked(true);
        }else {
            buy.setChecked(false);
        }

        return view;
    }
}
