package com.hp.householdpolicies.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.Download;
import com.hp.householdpolicies.model.Honor;
import com.hp.householdpolicies.model.OptimalPushResult;

import org.xutils.ImageManager;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.List;

public class HonorAdapter extends BaseRecyclerAdapter<HonorAdapter.SimpleAdapterViewHolder> {
    private Context mContext;
    private List<Honor> list;
    private static HonorAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * @param list
     * @param context
     */
    public HonorAdapter(List<Honor> list, Context context) {
        this.list = list;
    }

    //设置点击和长按事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件


    }

    public static void setOnItemClickListener(HonorAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final HonorAdapter.SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        Honor task = list.get(position);
        holder.tvName.setText(task.getTitle());
        ImageManager image = x.image();
        image.bind(holder.imgIcon, task.getImage(), new Callback.CacheCallback<Drawable>() {
            @Override
            public boolean onCache(Drawable result) {
                return false;
            }

            @Override
            public void onSuccess(Drawable result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        if(task.getShow()){
            holder.tvName.setVisibility(View.VISIBLE);
        }else{
            holder.tvName.setVisibility(View.INVISIBLE);
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
    public HonorAdapter.SimpleAdapterViewHolder getViewHolder(View view) {
        return new HonorAdapter.SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<Honor> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public HonorAdapter.SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_honor_item, parent, false);
        HonorAdapter.SimpleAdapterViewHolder vh = new HonorAdapter.SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Honor honor, int position) {
        insert(list, honor, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView imgIcon;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            }

        }
    }

    public Honor getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }
}
