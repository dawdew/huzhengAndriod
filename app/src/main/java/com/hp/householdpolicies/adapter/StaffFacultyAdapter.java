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
import com.hp.householdpolicies.model.Honor;

import java.util.List;

public class StaffFacultyAdapter extends BaseRecyclerAdapter<StaffFacultyAdapter.SimpleAdapterViewHolder> {
    private Context mContext;
    private List<String> list;
    private static StaffFacultyAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * @param list
     * @param context
     */
    public StaffFacultyAdapter(List<String> list, Context context) {
        this.list = list;
    }

    //设置点击和长按事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件


    }

    public static void setOnItemClickListener(StaffFacultyAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final StaffFacultyAdapter.SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        String task = list.get(position);
//        holder.tvName.setText("天津市先进集体");
//        if(task.getShow()){
//            holder.tvName.setVisibility(View.VISIBLE);
//        }else{
//            holder.tvName.setVisibility(View.INVISIBLE);
//        }


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
    public StaffFacultyAdapter.SimpleAdapterViewHolder getViewHolder(View view) {
        return new StaffFacultyAdapter.SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public StaffFacultyAdapter.SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_staff_faculty_item, parent, false);
        StaffFacultyAdapter.SimpleAdapterViewHolder vh = new StaffFacultyAdapter.SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(String str, int position) {
        insert(list, str, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            }

        }
    }

    public String getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }
}
