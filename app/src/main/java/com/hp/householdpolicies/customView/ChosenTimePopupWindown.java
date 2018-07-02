package com.hp.householdpolicies.customView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hp.householdpolicies.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChosenTimePopupWindown extends PopupWindow implements View.OnClickListener{
    SimpleDateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd");
    private Context mContext;
    private IChosenListener mchosenListener;
    DatePicker dp_date;
    TextView tv_cancel;//取消
    TextView tv_affirm;//确定
    private String time;

    @Override
    public void onClick(View view) {
        dismiss();
        if (mchosenListener != null) {

            mchosenListener.onItemClick(time);
        }
    }

    public static interface IChosenListener{
        public void onItemClick(String time);
    }

    public ChosenTimePopupWindown(Context context) {
        super();
        mContext = context;
        init();
    }
    public void setData(String strTime){
        time=strTime;
        try {

            Date date=formatFrom.parse(strTime);

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dp_date.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    time = year + "-" + formatTime(monthOfYear + 1)
                            + "-" + formatTime(dayOfMonth);
                }
            });
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_time_picker, null);
        setContentView(view);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
//                if (popupWindow != null && popupWindow.isShowing()) {
//                    popupWindow.dismiss();
//                    popupWindow = null;
//                }
                dismiss();
                return false;
            }
        });

         dp_date = (DatePicker) view.findViewById(R.id.dp_date);
        // 不显示日历视图
        dp_date.setCalendarViewShown(false);
        tv_cancel=view.findViewById(R.id.tv_cancel);
        tv_affirm=view.findViewById(R.id.tv_affirm);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_affirm.setOnClickListener(this);
//        tv_affirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }
    public void setItemListener(ChosenTimePopupWindown.IChosenListener listener) {
        mchosenListener = listener;
    }
    private String formatTime(int t) {
        return t >= 10 ? ("" + t) : ("0" + t);// 三元运算符 t>10时取 ""+t
    }
}
