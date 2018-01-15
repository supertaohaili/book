package com.thl.reader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thl.reader.Config;
import com.thl.reader.util.DisplayUtils;
import com.thl.reader.view.CircleImageView;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class SettingDialog extends Dialog implements View.OnClickListener {

    TextView tv_dark;
    SeekBar sb_brightness;
    TextView tv_bright;
    TextView tv_xitong;
    TextView tv_subtract;
    TextView tv_size;
    TextView tv_add;
    CircleImageView iv_bg_default;
    CircleImageView iv_bg1;
    CircleImageView iv_bg2;
    CircleImageView iv_bg3;
    CircleImageView iv_bg4;
    TextView tv_size_default;

    private Config config;
    private Boolean isSystem;
    private SettingListener mSettingListener;
    private int FONT_SIZE_MIN;
    private int FONT_SIZE_MAX;
    private int currentFontSize;

    private SettingDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public SettingDialog(Context context) {
        this(context, com.thl.reader.R.style.setting_dialog);
    }

    public SettingDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(com.thl.reader.R.layout.dialog_setting);

        tv_dark = (TextView) findViewById(com.thl.reader.R.id.tv_dark);
        sb_brightness = (SeekBar) findViewById(com.thl.reader.R.id.sb_brightness);
        tv_bright = (TextView) findViewById(com.thl.reader.R.id.tv_bright);
        tv_xitong = (TextView) findViewById(com.thl.reader.R.id.tv_xitong);
        tv_subtract = (TextView) findViewById(com.thl.reader.R.id.tv_subtract);
        tv_size = (TextView) findViewById(com.thl.reader.R.id.tv_size);
        tv_add = (TextView) findViewById(com.thl.reader.R.id.tv_add);
        iv_bg_default = (CircleImageView) findViewById(com.thl.reader.R.id.iv_bg_default);
        iv_bg1 = (CircleImageView) findViewById(com.thl.reader.R.id.iv_bg_1);
        iv_bg2 = (CircleImageView) findViewById(com.thl.reader.R.id.iv_bg_2);
        iv_bg3 = (CircleImageView) findViewById(com.thl.reader.R.id.iv_bg_3);
        iv_bg4 = (CircleImageView) findViewById(com.thl.reader.R.id.iv_bg_4);
        tv_size_default = (TextView) findViewById(com.thl.reader.R.id.tv_size_default);


        findViewById(com.thl.reader.R.id.tv_dark).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.tv_bright).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.tv_xitong).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.tv_subtract).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.tv_add).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.tv_size_default).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.iv_bg_default).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.iv_bg_1).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.iv_bg_2).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.iv_bg_3).setOnClickListener(this);
        findViewById(com.thl.reader.R.id.iv_bg_4).setOnClickListener(this);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);

        FONT_SIZE_MIN = (int) getContext().getResources().getDimension(com.thl.reader.R.dimen.reading_min_text_size);
        FONT_SIZE_MAX = (int) getContext().getResources().getDimension(com.thl.reader.R.dimen.reading_max_text_size);

        config = Config.getInstance();

        //初始化亮度
        isSystem = config.isSystemLight();
        setTextViewSelect(tv_xitong, isSystem);
        setBrightness(config.getLight());

        //初始化字体大小
        currentFontSize = (int) config.getFontSize();
        tv_size.setText(currentFontSize + "");

        //初始化字体
        selectBg(config.getBookBgType());

        sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 10) {
                    changeBright(false, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //选择背景
    private void selectBg(int type) {
        switch (type) {
            case Config.BOOK_BG_DEFAULT:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_1:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_2:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_3:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_4:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                break;
        }
    }

    //设置字体
    public void setBookBg(int type) {
        config.setBookBg(type);
        if (mSettingListener != null) {
            mSettingListener.changeBookBg(type);
        }
    }


    //设置亮度
    public void setBrightness(float brightness) {
        sb_brightness.setProgress((int) (brightness * 100));
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

    private void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    public Boolean isShow() {
        return isShowing();
    }


    public void onClick(View view) {
        int i = view.getId();
        if (i == com.thl.reader.R.id.tv_dark) {
        } else if (i == com.thl.reader.R.id.tv_bright) {
        } else if (i == com.thl.reader.R.id.tv_xitong) {
            isSystem = !isSystem;
            changeBright(isSystem, sb_brightness.getProgress());

        } else if (i == com.thl.reader.R.id.tv_subtract) {
            subtractFontSize();

        } else if (i == com.thl.reader.R.id.tv_add) {
            addFontSize();

        } else if (i == com.thl.reader.R.id.tv_size_default) {
            defaultFontSize();

        } else if (i == com.thl.reader.R.id.iv_bg_default) {
            setBookBg(Config.BOOK_BG_DEFAULT);
            selectBg(Config.BOOK_BG_DEFAULT);

        } else if (i == com.thl.reader.R.id.iv_bg_1) {
            setBookBg(Config.BOOK_BG_1);
            selectBg(Config.BOOK_BG_1);

        } else if (i == com.thl.reader.R.id.iv_bg_2) {
            setBookBg(Config.BOOK_BG_2);
            selectBg(Config.BOOK_BG_2);

        } else if (i == com.thl.reader.R.id.iv_bg_3) {
            setBookBg(Config.BOOK_BG_3);
            selectBg(Config.BOOK_BG_3);

        } else if (i == com.thl.reader.R.id.iv_bg_4) {
            setBookBg(Config.BOOK_BG_4);
            selectBg(Config.BOOK_BG_4);

        }
    }

    //变大书本字体
    private void addFontSize() {
        if (currentFontSize < FONT_SIZE_MAX) {
            currentFontSize += 1;
            tv_size.setText(currentFontSize + "");
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    private void defaultFontSize() {
        currentFontSize = (int) getContext().getResources().getDimension(com.thl.reader.R.dimen.reading_default_text_size);
        tv_size.setText(currentFontSize + "");
        config.setFontSize(currentFontSize);
        if (mSettingListener != null) {
            mSettingListener.changeFontSize(currentFontSize);
        }
    }

    //变小书本字体
    private void subtractFontSize() {
        if (currentFontSize > FONT_SIZE_MIN) {
            currentFontSize -= 1;
            tv_size.setText(currentFontSize + "");
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    //改变亮度
    public void changeBright(Boolean isSystem, int brightness) {
        float light = (float) (brightness / 100.0);
        setTextViewSelect(tv_xitong, isSystem);
        config.setSystemLight(isSystem);
        config.setLight(light);
        if (mSettingListener != null) {
            mSettingListener.changeSystemBright(isSystem, light);
        }
    }

    public void setSettingListener(SettingListener settingListener) {
        this.mSettingListener = settingListener;
    }

    public interface SettingListener {
        void changeSystemBright(Boolean isSystem, float brightness);

        void changeFontSize(int fontSize);

        void changeTypeFace(Typeface typeface);

        void changeBookBg(int type);
    }

}