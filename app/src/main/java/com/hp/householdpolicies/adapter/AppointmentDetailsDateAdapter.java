package com.hp.householdpolicies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.AppDate;
import com.hp.householdpolicies.model.Salesman;

import java.util.List;

public class AppointmentDetailsDateAdapter extends BaseRecyclerAdapter<AppointmentDetailsDateAdapter.SimpleAdapterViewHolder> {
    private Context mContext;
    private List<AppDate> list;
    private int mPosition=-1;
    private static AppointmentDetailsDateAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * @param list
     * @param context
     */
    public AppointmentDetailsDateAdapter(List<AppDate> list, Context context) {
        this.list = list;
    }

    //设置点击和长按事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件


    }

    public static void setOnItemClickListener(AppointmentDetailsDateAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final AppointmentDetailsDateAdapter.SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        final AppDate date = list.get(position);
        holder.tvDate.setText(date.getDateStr());
        if(mPosition==position){
            holder.tvDate.setBackgroundResource(R.color.date);
        }else {
            holder.tvDate.setBackgroundResource(R.color.white);
        }
        if(!date.getAvailable()){
            holder.tvDate.setBackgroundResource(R.mipmap.ic_line_bg);
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() { //itemview是holder里的所有控件，可以单独设置比如ImageButton Button等
                @Override
                public void onClick(View v) {
                    mPosition =position;
                    notifyDataSetChanged();
                    holder.tvDate.setBackgroundResource(R.color.date);
                    if(date.getAvailable()){
                        mOnItemClickListener.onItemClick(holder.tvDate, position);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { //长按事件
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.tvDate, position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public AppointmentDetailsDateAdapter.SimpleAdapterViewHolder getViewHolder(View view) {
        return new AppointmentDetailsDateAdapter.SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<AppDate> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public AppointmentDetailsDateAdapter.SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_appointment_details_date_item, parent, false);
        AppointmentDetailsDateAdapter.SimpleAdapterViewHolder vh = new AppointmentDetailsDateAdapter.SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(AppDate str, int position) {
        insert(list, str, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;//日期

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            }

        }
    }

    public AppDate getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

}
