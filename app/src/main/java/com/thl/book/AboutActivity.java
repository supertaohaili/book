package com.thl.book;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.thl.book.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected int initLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("关于");
        findViewById(R.id.ib_back).setVisibility(View.VISIBLE);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvMsg = (TextView) findViewById(R.id.tv_msg);
        tvMsg.setMovementMethod(LinkMovementMethod.getInstance());
        tvMsg.setText("软件说明：\n\t\t\t\t一款干净美观的本地电子书阅读器,软件界面简洁，无广告。本软件作为开源项目进行开发，一起努力吧 ! ! !\t\t\t\t" +
                "\n\n获取源码地址:\n \t\t\t\thttps://my.oschina.net/u/2440407/blog/1607740");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        findViewById(R.id.tv_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:1312398581@qq.com"));
                data.putExtra(Intent.EXTRA_SUBJECT,  "本地图书意见反馈");
                data.putExtra(Intent.EXTRA_TEXT, "建议：1、");
                startActivity(data);
            }
        });
    }


}
