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

import java.util.List;
import java.util.Map;

public class DepartmentsSearchAdapter extends BaseRecyclerAdapter<DepartmentsSearchAdapter.SimpleAdapterViewHolder> {
    private Context mContext;
    private List<Map<String,String>> list;
    private static DepartmentsSearchAdapter.OnItemClickListener mOnItemClickListener;

    /**
     *
     * @param list
     * @param context
     */
    public DepartmentsSearchAdapter(List<Map<String,String>> list, Context context) {
        this.list = list;
    }

    //设置点击和长按事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position,Map<String,String> item); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件

    }

    public static void setOnItemClickListener(DepartmentsSearchAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final DepartmentsSearchAdapter.SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        final Map<String,String> task = list.get(position);
        holder.tv_name.setText(task.get("name"));
        switch (position%5){
            case 0:
                holder.imgState.setImageResource(R.drawable.state_red);
                break;
            case 1:
                holder.imgState.setImageResource(R.drawable.state_green);
                break;
            case 2:
                holder.imgState.setImageResource(R.drawable.state_blue);
                break;
            case 3:
                holder.imgState.setImageResource(R.drawable.state_violet);
                break;
            case 4:
                holder.imgState.setImageResource(R.drawable.state_orange);
                break;
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() { //itemview是holder里的所有控件，可以单独设置比如ImageButton Button等
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position,task);
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
    public DepartmentsSearchAdapter.SimpleAdapterViewHolder getViewHolder(View view) {
        return new DepartmentsSearchAdapter.SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<Map<String,String>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public DepartmentsSearchAdapter.SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_departments_search_item, parent, false);
        DepartmentsSearchAdapter.SimpleAdapterViewHolder vh = new DepartmentsSearchAdapter.SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Map<String,String> person, int position) {
        insert(list, person, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private ImageView imgState;//左侧状态
        private ImageView imgRecommend;//推荐图片

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                tv_name = (TextView) itemView.findViewById(R.id.tvName);
                imgState=(ImageView) itemView.findViewById(R.id.imgState);
                imgRecommend=(ImageView) itemView.findViewById(R.id.imgRecommend);
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
