package com.hp.householdpolicies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.hp.householdpolicies.R;

import java.util.List;
import java.util.Map;

public class DepartmentsOneAdapter extends BaseRecyclerAdapter<DepartmentsOneAdapter.SimpleAdapterViewHolder> {
    private Context mContext;
    private List<Map<String,String>> list;
    private static DepartmentsOneAdapter.OnItemClickListener mOnItemClickListener;

    /**
     *
     * @param list
     * @param context
     */
    public DepartmentsOneAdapter(List<Map<String,String>> list, Context context) {
        this.list = list;
    }

    //设置点击和长按事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position,Map<String,String> item); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件

        void onDownloadClick(View view, int position);//下载事件

    }

    public static void setOnItemClickListener(DepartmentsOneAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final DepartmentsOneAdapter.SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        final Map<String,String> item = list.get(position);
        holder.tvConten.setText(item.get("name"));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView,position,item);
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
    public DepartmentsOneAdapter.SimpleAdapterViewHolder getViewHolder(View view) {
        return new DepartmentsOneAdapter.SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<Map<String,String>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public DepartmentsOneAdapter.SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_departments_one_item, parent, false);
        DepartmentsOneAdapter.SimpleAdapterViewHolder vh = new DepartmentsOneAdapter.SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Map<String,String> download, int position) {
        insert(list, download, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvConten;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                tvConten = (TextView) itemView.findViewById(R.id.tvConten);
            }

        }
    }

    public Map<String,String> getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }
}
