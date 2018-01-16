package com.thl.book;

import android.support.v7.app.AppCompatActivity;
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
                "稍加处理，即可实现各种梦幻功能。" +
                "点击跳转github: https://github.com/supertaohaili/book，获取最近源码吧");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


}
