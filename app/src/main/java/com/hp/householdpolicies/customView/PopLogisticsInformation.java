package com.hp.householdpolicies.customView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.LogisticsInformation;

import java.util.ArrayList;
import java.util.List;

public class PopLogisticsInformation extends PopupWindow implements
        AdapterView.OnItemClickListener {
    private Context mContext;
    private ListView mListView;
    private TextView tv_express_number;//快递单号
    private LinearLayout llContent;
    private List<LogisticsInformation> list_valuse = new ArrayList<LogisticsInformation>();
    private String ExpressNumber = "";
    private IOnItemSelectListener mItemSelectListener;
    MyAdapter mAdapter;

    public static interface IOnItemSelectListener {
        public void onItemClick(int pos);
    }

    public PopLogisticsInformation(Context context) {
        super();
        mContext=context;
        init();
    }

    public void setData(List<LogisticsInformation> values, String strExpressNumber) {
        list_valuse = values;
        mAdapter.notifyDataSetChanged();
        ExpressNumber = strExpressNumber;
        tv_express_number.setText("快递单号：" + ExpressNumber);

    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_logistics_information, null);
        setContentView(view);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setFocusable(false);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

        mListView = (ListView) view.findViewById(R.id.lv);
        tv_express_number = (TextView) view.findViewById(R.id.tv_express_number);
        llContent=(LinearLayout) view.findViewById(R.id.llContent);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void setItemListener(IOnItemSelectListener listener) {
        mItemSelectListener = listener;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        dismiss();
        if (mItemSelectListener != null) {

            mItemSelectListener.onItemClick(position);
        }
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public MyAdapter() {
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
                        R.layout.pop_logistics_information_item, null);
                holder.tv_content = (TextView) convertView
                        .findViewById(R.id.tv_content);
                holder.tv_Time=(TextView) convertView.findViewById(R.id.tv_Time);
                holder.upLine=(View) convertView.findViewById(R.id.upLine);
                holder.belowLine=(View) convertView.findViewById(R.id.belowLine);
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }
            if(position==0){
                holder.upLine.setVisibility(View.INVISIBLE);
            }else{
                holder.upLine.setVisibility(View.VISIBLE);
            }
            if(position==(list_valuse.size()-1)){
                holder.belowLine.setVisibility(View.INVISIBLE);
            }else{
                holder.belowLine.setVisibility(View.VISIBLE);
            }
            holder.tv_content.setText(list_valuse.get(position).getContent());
            holder.tv_Time.setText(list_valuse.get(position).getTime());
            return convertView;
        }

        private class viewHolder {
            private TextView tv_content;//快递信息
            private TextView tv_Time;//时间
            private View upLine;//上线
            private View belowLine;//下线

        }
    }

}
