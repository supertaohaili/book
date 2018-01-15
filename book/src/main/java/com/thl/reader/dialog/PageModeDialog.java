package com.thl.reader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.thl.reader.Config;

/**
 * Created by Administrator on 2016/8/30 0030.
 */
public class PageModeDialog extends Dialog implements View.OnClickListener {

    TextView tv_simulation;
    TextView tv_cover;
    TextView tv_slide;
    TextView tv_none;

    private Config config;
    private PageModeListener pageModeListener;

    private PageModeDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public PageModeDialog(Context context) {
        this(context, com.thl.reader.R.style.setting_dialog);
    }

    public PageModeDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(com.thl.reader.R.layout.dialog_pagemode);

        tv_simulation = (TextView) findViewById(com.thl.reader.R.id.tv_simulation);
        tv_cover = (TextView) findViewById(com.thl.reader.R.id.tv_cover);
        tv_slide = (TextView) findViewById(com.thl.reader.R.id.tv_slide);
        tv_none = (TextView) findViewById(com.thl.reader.R.id.tv_none);


        findViewById(com.thl.reader.R.id.tv_simulation).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.tv_cover).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.tv_slide).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.tv_none).setOnClickListener(this);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);

        config = Config.getInstance();
        selectPageMode(config.getPageMode());
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == com.thl.reader.R.id.tv_simulation) {
            selectPageMode(Config.PAGE_MODE_SIMULATION);
            setPageMode(Config.PAGE_MODE_SIMULATION);

        } else if (i == com.thl.reader.R.id.tv_cover) {
            selectPageMode(Config.PAGE_MODE_COVER);
            setPageMode(Config.PAGE_MODE_COVER);

        } else if (i == com.thl.reader.R.id.tv_slide) {
            selectPageMode(Config.PAGE_MODE_SLIDE);
            setPageMode(Config.PAGE_MODE_SLIDE);

        } else if (i == com.thl.reader.R.id.tv_none) {
            selectPageMode(Config.PAGE_MODE_NONE);
            setPageMode(Config.PAGE_MODE_NONE);

        }
    }

    //设置翻页
    public void setPageMode(int pageMode) {
        config.setPageMode(pageMode);
        if (pageModeListener != null) {
            pageModeListener.changePageMode(pageMode);
        }
    }

    //选择怕翻页
    private void selectPageMode(int pageMode) {
        if (pageMode == Config.PAGE_MODE_SIMULATION) {
            setTextViewSelect(tv_simulation, true);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_COVER) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, true);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_SLIDE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, true);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_NONE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, true);
        }
    }

    //设置按钮选择的背景
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(com.thl.reader.R.drawable.button_select_bg));
            textView.setTextColor(getContext().getResources().getColor(com.thl.reader.R.color.read_dialog_button_select));
        } else {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(com.thl.reader.R.drawable.button_bg));
            textView.setTextColor(getContext().getResources().getColor(com.thl.reader.R.color.white));
        }
    }

    public void setPageModeListener(PageModeListener pageModeListener) {
        this.pageModeListener = pageModeListener;
    }

    public interface PageModeListener {
        void changePageMode(int pageMode);
    }
}
