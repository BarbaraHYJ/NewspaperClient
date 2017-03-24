package com.example.barbara.newspapersub.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.barbara.newspapersub.R;

import java.util.List;
import java.util.Map;

/**
 * Created by barbara on 2016/12/30.
 */
public class ListEditorAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData;// 存储的EditText值

    public ListEditorAdapter(Context context, List<Map<String, Object>> data) {
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
        ViewHolder holder = null;
        // convertView为null的时候初始化convertView。
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_create_order, null);
            holder.news_id = (TextView) convertView.findViewById(R.id.item_news_id);
            holder.copies = (EditText) convertView.findViewById(R.id.item_et_copies);
            holder.months = (EditText) convertView.findViewById(R.id.item_et_months);
            holder.copies.addTextChangedListener(new CopiesWatcher(holder));
            holder.months.addTextChangedListener(new MonthsWatcher(holder));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.copies.setTag(position);
            holder.months.setTag(position);
        }

        Object value = mData.get(position).get("news_id");
        if (value != null) {
            holder.news_id.setText(String.valueOf(value));
        }

        return convertView;
    }

    public final class ViewHolder {
        public TextView news_id;
        public EditText copies;
        public EditText months;
    }

    class CopiesWatcher implements TextWatcher {
        public CopiesWatcher(ViewHolder holder) {
            mHolder = holder;
        }

        private ViewHolder mHolder;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {
            int position = (Integer) mHolder.copies.getTag();

            if(s == null || "".equals(s.toString())) {
                mData.get(position).put("copies",
                        s.toString());
            }
            if (s != null && !"".equals(s.toString())) {
                mData.get(position).put("copies",
                        s.toString());// 当EditText数据发生改变的时候存到data变量中
            }
        }
    }

    class MonthsWatcher implements TextWatcher {
        public MonthsWatcher(ViewHolder holder) {
            mHolder = holder;
        }

        private ViewHolder mHolder;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {
            int position = (Integer) mHolder.months.getTag();

            if(s == null || "".equals(s.toString())) {
                mData.get(position).put("months",
                        s.toString());
            }
            if (s != null && !"".equals(s.toString())) {
                mData.get(position).put("months",
                        s.toString());// 当EditText数据发生改变的时候存到data变量中
            }
        }
    }
}
