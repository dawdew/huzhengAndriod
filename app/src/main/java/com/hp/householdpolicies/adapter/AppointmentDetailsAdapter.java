package com.hp.householdpolicies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.Salesman;

import java.util.List;

public class AppointmentDetailsAdapter extends BaseRecyclerAdapter<AppointmentDetailsAdapter.SimpleAdapterViewHolder> {
    private Context mContext;
    private List<Salesman> list;
    private static OnItemClickListener mOnItemClickListener;

    /**
     * @param list
     * @param context
     */
    public AppointmentDetailsAdapter(List<Salesman> list, Context context) {
        this.list = list;
    }

    //设置点击和长按事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件


    }

    public static void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final AppointmentDetailsAdapter.SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        Salesman salesman = list.get(position);
        holder.tvName.setText("姓名：" + salesman.getName());
        holder.tvSiren.setText("警号：" + salesman.getSiren());
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() { //itemview是holder里的所有控件，可以单独设置比如ImageButton Button等
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { //长按事件
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
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
    public AppointmentDetailsAdapter.SimpleAdapterViewHolder getViewHolder(View view) {
        return new AppointmentDetailsAdapter.SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<Salesman> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public AppointmentDetailsAdapter.SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_appointment_details_item, parent, false);
        AppointmentDetailsAdapter.SimpleAdapterViewHolder vh = new AppointmentDetailsAdapter.SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Salesman str, int position) {
        insert(list, str, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon;//头像
        private TextView tvName;//姓名
        private TextView tvSiren;//警号

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvSiren = (TextView) itemView.findViewById(R.id.tvSiren);
            }

        }
    }

    public Salesman getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

}
