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
import com.hp.householdpolicies.model.StaffFaculty;
import com.hp.householdpolicies.utils.Api;

import org.xutils.ImageManager;
import org.xutils.x;

import java.util.List;
import java.util.Map;

public class StaffFacultyNewAdapter  extends BaseRecyclerAdapter<StaffFacultyNewAdapter.SimpleAdapterViewHolder> {
private Context mContext;
private List<Map> list;
private static StaffFacultyNewAdapter.OnItemClickListener mOnItemClickListener;
    ImageManager image = x.image();
/**
 * @param list
 * @param context
 */
public StaffFacultyNewAdapter(List<Map> list, Context context) {
        this.list = list;
        }

//设置点击和长按事件
public interface OnItemClickListener {
    void onItemClick(View view, int position); //设置点击事件

    void onItemLongClick(View view, int position); //设置长按事件


}

    public static void setOnItemClickListener(StaffFacultyNewAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final StaffFacultyNewAdapter.SimpleAdapterViewHolder holder, final int position, boolean isItem) {
        Map staffFaculty = list.get(position);
        String roleName = (String) staffFaculty.get("roleName");
        if("clerk".equals(roleName)){
            holder.tvSiren.setText("编号："+staffFaculty.get("pno"));
        }else if("police".equals(roleName)){
            holder.tvSiren.setText("警号："+staffFaculty.get("pno"));
        }else {
            holder.tvSiren.setText("");
        }
        holder.tvName.setText("姓名："+staffFaculty.get("realName"));
        image.bind(holder.imgIcon, Api.baseFile+staffFaculty.get("photo"));
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
    public StaffFacultyNewAdapter.SimpleAdapterViewHolder getViewHolder(View view) {
        return new StaffFacultyNewAdapter.SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<Map> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public StaffFacultyNewAdapter.SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_staff_faculty_new_item, parent, false);
        StaffFacultyNewAdapter.SimpleAdapterViewHolder vh = new StaffFacultyNewAdapter.SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Map str, int position) {
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
    private TextView tvName;//姓名
    private TextView tvSiren;//警号

    public SimpleAdapterViewHolder(View itemView, boolean isItem) {
        super(itemView);
        if (isItem) {
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            tvName=(TextView) itemView.findViewById(R.id.tvName);
            tvSiren=(TextView) itemView.findViewById(R.id.tvSiren);
        }

    }
}

    public Map getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }
}
