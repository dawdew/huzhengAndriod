package com.hp.householdpolicies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.Download;
import com.hp.householdpolicies.model.OptimalPushResult;

import java.util.List;

public class DownloadAdapter extends BaseRecyclerAdapter<DownloadAdapter.SimpleAdapterViewHolder> {
    private Context mContext;
    private List<Download> list;
    private static DownloadAdapter.OnItemClickListener mOnItemClickListener;

    /**
     *
     * @param list
     * @param context
     */
    public DownloadAdapter(List<Download> list, Context context) {
        this.list = list;
    }

    //设置点击和长按事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件

        void onDownloadClick(View view,int position,String url);//下载事件

    }

    public static void setOnItemClickListener(DownloadAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final DownloadAdapter.SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        final Download task = list.get(position);
        holder.tvConten.setText(task.getContent());

        if (mOnItemClickListener != null) {
//            holder.tv_name.setOnClickListener(new View.OnClickListener() { //itemview是holder里的所有控件，可以单独设置比如ImageButton Button等
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(holder.itemView, position);
//                }
//            });
//            holder.tv_name.setOnLongClickListener(new View.OnLongClickListener() { //长按事件
//                @Override
//                public boolean onLongClick(View v) {
//                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
//                    return false;
//                }
//            });
            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onDownloadClick(v,position,task.getUrl());
                }
            });
        }

        //设置是否可以侧滑删除
//        ((SwipeMenuLayout) holder.itemView).setLeftSwipe(isDel).setSwipeEnable(isDel);
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
    public DownloadAdapter.SimpleAdapterViewHolder getViewHolder(View view) {
        return new DownloadAdapter.SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<Download> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public DownloadAdapter.SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_download_item, parent, false);
        DownloadAdapter.SimpleAdapterViewHolder vh = new DownloadAdapter.SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Download download, int position) {
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
        private Button btnDownload;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                tvConten = (TextView) itemView.findViewById(R.id.tvConten);
                btnDownload=(Button) itemView.findViewById(R.id.btnDownload);
            }

        }
    }

    public Download getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }
}