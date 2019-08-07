package com.hp.householdpolicies.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.ArticleAdapter;
import com.hp.householdpolicies.adapter.HonorAdapter;
import com.hp.householdpolicies.customView.DownloadPopupWindown;
import com.hp.householdpolicies.model.Article;
import com.hp.householdpolicies.model.Honor;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;
import com.iflytek.cloud.SpeechSynthesizer;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvisoryDetailsActivity extends BaseActivity {
    //便签打印
    @BindView(R.id.llPrint)
    LinearLayout llPrint;
    //一年内
//    @BindView(R.id.btnYear)
//    Button btnYear;
//    //一年以上
//    @BindView(R.id.btnMoreYear)
//    Button btnMoreYear;
    Context mContext;
    //列表
    @BindView(R.id.adCategory)
    RelativeLayout adCategory;
    @BindView(R.id.rcv)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.webview)
    WebView webView;
    private List<Article> listArticle = new ArrayList<Article>();
    private ArticleAdapter adapter;
    private DownloadPopupWindown popupWindown;
    private SpeechSynthesizer mTts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentLayout(R.layout.activity_advisory_details);
        ButterKnife.bind(this);
        WebSettings settings=webView.getSettings();
        settings.setTextSize(WebSettings.TextSize.LARGER);
        webView.setBackgroundColor(0);
        popupWindown=new DownloadPopupWindown(mContext);
        MyApp application = (MyApp) getApplication();
        mTts = application.getmTts();
        mTts.startSpeaking("请点击左侧列表选择相应政策",null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ArticleAdapter(listArticle, mContext);
        recyclerView.setAdapter(adapter);
        getData();
        adapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,Article a) {
                title.setText(a.getTitle());
                webView.loadDataWithBaseURL(null,a.getContent(), "text/html" , "utf-8", null);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                System.out.println(position);
            }
        });
//        btnYear.setSelected(true);
    }

    @OnClick({R.id.llPrint})
    void click(View view) {
        String s = title.getText().toString();

        showLogisticsInformationWindow(view,s+".docx");
    }
    private void getData(){
        final Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        if(StringUtils.isNotBlank(category)){
            HashMap<String, String> map = new HashMap<>();
            map.put("cpid",category);
            OkhttpUtil.okHttpGet(Api.articleList, map, new ApiCallBack() {
                @Override
                public void onResponse(Object response) {
                    List<Map<String, String>> list = (List<Map<String, String>>) response;
                    String word = getIntent().getStringExtra("word");
                    boolean show_flag=false;
                    int pos=0;
                    for(Map<String, String> m:list){
                        Article article = new Article();
                        article.setTitle(m.get("title"));
                        article.setCname(m.get("cname"));
                        article.setContent(m.get("content"));
                        if(StringUtils.isNotBlank(word)){
                            if(m.get("cname").contains(word)){
                                show_flag=true;
                                pos = listArticle.size();
                                title.setText(article.getTitle());
                                webView.loadDataWithBaseURL(null,article.getContent(), "text/html" , "utf-8", null);
                            }
                        }
                        listArticle.add(article);
                    }
                    if(!show_flag && list!=null && list.size()>0){
                        title.setText(list.get(0).get("title"));
                        webView.loadDataWithBaseURL(null,list.get(0).get("content"), "text/html" , "utf-8", null);
                        if(StringUtils.isNotBlank(word)){
                            mTts.startSpeaking("这是我为您找到的相关户籍政策，请根据需要选择适合的类别",null);
                        }
                    }
                    adapter.setSelectedPos(pos);
                    adapter.notifyDataSetChanged();
                }
            });
        }else {
            adCategory.setVisibility(View.GONE);
            String titleStr = intent.getStringExtra("title");
            String contentStr = intent.getStringExtra("content");
            Article article = new Article();
            article.setTitle(titleStr);
            article.setCname(titleStr);
            article.setContent(contentStr);
            listArticle.add(article);
            title.setText(titleStr);
            webView.loadDataWithBaseURL(null,contentStr, "text/html" , "utf-8", null);
            adapter.notifyDataSetChanged();
        }
  }
    //扫一扫下载窗口
    private void showLogisticsInformationWindow(View v,String url) {
        popupWindown.setData(Api.downurl+url);
        popupWindown.setTouchable(true);
        popupWindown.setOutsideTouchable(true);
        popupWindown.setFocusable(true);
        popupWindown.setBackgroundDrawable(new BitmapDrawable());
        popupWindown.showAtLocation(v, Gravity.CENTER, 0, 0);

    }
}
