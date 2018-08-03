package com.hp.householdpolicies.customView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hp.householdpolicies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/5 0005.
 */

public class SpinnerPopupWindown extends PopupWindow implements
        AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private TextView textView;
    private List<String> list_valuse=new ArrayList<String>();
    private IOnItemSelectListener mItemSelectListener;
    AbstractSpinerAdapter mAdapter;

    public static interface IOnItemSelectListener{
        public void onItemClick(int pos);
    }

    public SpinnerPopupWindown(Context context, List<String> values) {
        super();
        mContext = context;
        init();
    }
    public void setData(List<String> values){
        list_valuse=values;
        mAdapter.notifyDataSetChanged();

    }
    public void setData(List<String> values,TextView textView){
        list_valuse=values;
        this.textView=textView;
        mAdapter.notifyDataSetChanged();

    }
    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_spinner_layout, null);
        setContentView(view);

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

        mListView = (ListView) view.findViewById(R.id.lv);

        mAdapter= new AbstractSpinerAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    public void setItemListener(IOnItemSelectListener listener) {
        mItemSelectListener = listener;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        dismiss();
        if(textView!=null){
            textView.setText(list_valuse.get(position));
        }
//        if (mItemSelectListener != null) {
//            mItemSelectListener.onItemClick(position);
//        }
    }

    private class AbstractSpinerAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public AbstractSpinerAdapter() {
            super();
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return list_valuse.size();
        }

        @Override
        public Object getItem(int position) {
            return list_valuse.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder holder;
            if (convertView == null) {
                holder = new viewHolder();
                convertView = inflater.inflate(
                        R.layout.pop_spinner_layout_item, null);
                holder.tv_content = (TextView) convertView
                        .findViewById(R.id.tv_content);
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }
            holder.tv_content.setText(list_valuse.get(position));
            return convertView;
        }

        private class viewHolder {
            private TextView tv_content;
        }
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
