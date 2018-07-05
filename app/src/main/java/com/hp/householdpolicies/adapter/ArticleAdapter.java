package com.hp.householdpolicies.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.Article;
import com.hp.householdpolicies.model.Honor;

import org.xutils.ImageManager;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.List;

public class ArticleAdapter extends BaseRecyclerAdapter<ArticleAdapter.SimpleAdapterViewHolder> {
    private Context mContext;
    private List<Article> list;
    private int selectedPos = 0;
    private static ArticleAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * @param list
     * @param context
     */
    public ArticleAdapter(List<Article> list, Context context) {
        this.list = list;
    }

    //设置点击和长按事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position,Article art); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件
    }

    public static void setOnItemClickListener(ArticleAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final ArticleAdapter.SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        final Article task = list.get(position);
        holder.btName.setText(task.getCname());
        if(selectedPos == position){
            holder.btName.setSelected(true);
        }else{
            holder.btName.setSelected(false);
        }
        if (mOnItemClickListener != null) {
            holder.btName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedPos);
                    selectedPos = position;
                    notifyItemChanged(selectedPos);
                    mOnItemClickListener.onItemClick(v,position,task);
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
    public ArticleAdapter.SimpleAdapterViewHolder getViewHolder(View view) {
        return new ArticleAdapter.SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<Article> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ArticleAdapter.SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_advisory_article, parent, false);
        ArticleAdapter.SimpleAdapterViewHolder vh = new ArticleAdapter.SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Article act, int position) {
        insert(list, act, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {
        private Button btName;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                btName = (Button) itemView.findViewById(R.id.btName);
            }

        }
    }

    public Article getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }
}
