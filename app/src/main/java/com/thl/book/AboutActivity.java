package com.thl.book;

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
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvMsg = (TextView) findViewById(R.id.tv_msg);
        tvMsg.setMovementMethod(LinkMovementMethod.getInstance());
        tvMsg.setText("\t\t\t\t 一款干净美观的本地电子书阅读器，为了世界和平，决定将代码开源，" +
                "稍加处理，即可实现各种梦幻功能。\n\n" +
                "如果你有任何问题/疑问/错误/拥抱请发邮件：taohailili@gmail.com，\n\n" +
                "获取源码地址:\n https://my.oschina.net/u/2440407/blog/1607740");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


}
