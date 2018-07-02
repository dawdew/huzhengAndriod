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
import com.hp.householdpolicies.model.OptimalPushResult;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;
//BaseRecyclerAdapter<OptimalPushResultsAdapter.SimpleAdapterViewHolder>
public class OptimalPushResultsAdapter extends BaseRecyclerAdapter<OptimalPushResultsAdapter.SimpleAdapterViewHolder> {
    private Context mContext;
    private List<OptimalPushResult> list;
    private static OnItemClickListener mOnItemClickListener;
    private Boolean isDel;//是否需要侧滑删除

    /**
     *
     * @param list
     * @param context
     * @param isDel----是否可以侧滑删除
     */
    public OptimalPushResultsAdapter(List<OptimalPushResult> list, Context context,Boolean isDel) {
        this.list = list;
        this.isDel = isDel;
    }

    //设置点击和长按事件
    public interface OnItemClickListener{
        void onItemClick(View view, OptimalPushResult opr); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件

        void onItemDel(int position);//删除事件

    }

    public static void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        final OptimalPushResult task = list.get(position);
        holder.tv_name.setText(task.getName());
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
        if(task.isRecommend()){
            holder.imgRecommend.setVisibility(View.VISIBLE);
        }else{
            holder.imgRecommend.setVisibility(View.INVISIBLE);
        }
        if (mOnItemClickListener != null) {
            holder.item.setOnClickListener(new View.OnClickListener() { //itemview是holder里的所有控件，可以单独设置比如ImageButton Button等
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, task);
                }
            });
//            holder.tv_name.setOnLongClickListener(new View.OnLongClickListener() { //长按事件
//                @Override
//                public boolean onLongClick(View v) {
//                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
//                    return false;
//                }
//            });
//            holder.btn_del.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemDel(position);
//                }
//            });
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
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<OptimalPushResult> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_optimal_push_results_item, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(OptimalPushResult person, int position) {
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
        private View item;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            item =itemView;
            if (isItem) {
                tv_name = (TextView) itemView.findViewById(R.id.tvName);
                imgState=(ImageView) itemView.findViewById(R.id.imgState);
                imgRecommend=(ImageView) itemView.findViewById(R.id.imgRecommend);
            }

        }
    }

    public OptimalPushResult getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }
}
