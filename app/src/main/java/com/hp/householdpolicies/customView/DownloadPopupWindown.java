package com.hp.householdpolicies.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.LogisticsInformation;
import com.hp.householdpolicies.utils.ZxingUtils;

import java.util.ArrayList;
import java.util.List;

public class DownloadPopupWindown extends PopupWindow {
    private Context mContext;
////    private ListView mListView;
//    private TextView tv_express_number;//快递单号
    private LinearLayout llContent;
    private ImageView downloadQr;
    private String downloadUrl = "";


    public DownloadPopupWindown(Context context) {
        super();
        mContext=context;
        init();
    }

    public void setData(String downloadUrl) {

        this.downloadUrl = downloadUrl;
        Bitmap bitmap = ZxingUtils.createBitmap(downloadUrl);
        downloadQr.setImageBitmap(bitmap);
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_download, null);
        setContentView(view);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setFocusable(false);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        llContent=(LinearLayout) view.findViewById(R.id.llContent);
        downloadQr=view.findViewById(R.id.downloadQr);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}