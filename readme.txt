# “本地电子书”介绍

一款干净美观的本地电子书阅读器，为了世界和平，决定将代码开源，稍加处理，即可实现各种梦幻功能。


# 效果图

 <img src="https://github.com/supertaohaili/fingerprint/blob/master/Screenshot_20180112-102257.png" width="300"><img src="https://github.com/supertaohaili/fingerprint/blob/master/Screenshot_20180112-102323.png" width="300">

apk下载链接
<a href="https://github.com/supertaohaili/fingerprint/blob/master/app-debug.apk">https://github.com/supertaohaili/fingerprint/blob/master/app-debug.apk</a>

# 使用
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
     compile 'com.github.supertaohaili:fingerprint:1.0.0'
}
```

示例代码:
``` java
 mFingerprintIdentify = new FingerprintIdentify(this, null);
  mFingerprintIdentify.startIdentify(MAX_AVAILABLE_TIMES, new BaseFingerprint.FingerprintIdentifyListener() {
             @Override
             public void onSucceed() {
                 Toast.makeText(MainActivity.this, "解锁成功", Toast.LENGTH_SHORT).show();
                 tvMsg.setTextColor(Color.parseColor("#ff333333"));
                 tvMsg.setText("解锁成功");
             }

             @Override
             public void onNotMatch(int availableTimes) {
                 Log.e("Fingerprint", "onNotMatch");
                 tvMsg.setTextColor(Color.parseColor("#ffff0101"));
                 tvMsg.setText("密码错了，还可输入" + availableTimes + "次");
                 translate(ivZhiwen);
             }

             @Override
             public void onFailed(boolean isDeviceLocked) {
                 tvMsg.setTextColor(Color.parseColor("#ffff0101"));
                 tvMsg.setText("指纹验证太过频繁，请稍后重试或者输入密码登录");
                 mTimeCount.start();
                 translate(ivZhiwen);
             }

             @Override
             public void onStartFailedByDeviceLocked() {
                 tvMsg.setTextColor(Color.parseColor("#ffff0101"));
                 tvMsg.setText("指纹验证太过频繁，请稍后重试或者输入密码登录");
                 mTimeCount.start();
                 translate(ivZhiwen);
             }
         });

    @Override
    public void onPause() {
        super.onPause();
        if (mFingerprintIdentify != null) {
            mFingerprintIdentify.cancelIdentify();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFingerprintIdentify != null) {
            mFingerprintIdentify.resumeIdentify();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFingerprintIdentify != null) {
            mFingerprintIdentify.cancelIdentify();
        }
    }

```

混淆文件
```java
# MeiZuFingerprint
-keep class com.fingerprints.service.** { *; }

# SmsungFingerprint
-keep class com.samsung.android.sdk.** { *; }
```

### Known Issues
If you have any questions/queries/Bugs/Hugs please mail @
taohailili@gmail.com



1、签名信息：
keystore=sign/video.jks
storePassword=123456
keyAlias=video
keyPassword=123456

2、添加了bugly 异常上报与更新，已经添加你的QQ为管理员，登录https://bugly.qq.com/v2/crash-reporting/dashboard/9bb43e127f?pid=1 可发布新版本与查看bug

3、添加了友盟统计，可登录http://passport.umeng.com/login  账号：1312398581@qq.com  密码：gxut123456

4、后台使用后端云服务Bmob：https://www.bmob.cn/  账号：15220055862 密码：15220055862   （用来处理会员模块）

5、使用jsoup 解析htmml ，纯原生，自己开线程去解析

6、使用glide 加载图片 ，已经使用策略模式封装好工具类

7、 使用PhotoView 实现图片预览放大缩小，框架地址：https://github.com/chrisbanes/PhotoView， 二次封装放在工具类包里面
调用：MNImageBrowser.showImageBrowser(Activity context, View view, int position, ArrayList<String> imageList)

8、对话框使用：https://github.com/mylhyl/Android-CircleDialog
对其做了样式的修改，并将“CircleDialog”类名改成：“DialogUtils”
       简单的对话框
                new DialogUtils.Builder(this)
                        .setTitle("标题")
                        .setText("提示框")
                        .setPositive("确定", null)
                        .show();

9、上下拉刷新：https://github.com/lcodecorex/TwinklingRefreshLayout（这个稳定，bug少）

10、视频播放使用腾讯的xwebview

11、项目全局使用RecyclerView，适配器的二次封装使用：（支持多种布局）
博客：http://blog.csdn.net/fisher0113/article/details/51955845
github:https://github.com/fishyer/StudyRecyclerView

12、数据库：使用litepal ，轻量，简单：https://github.com/LitePalFramework/LitePal

设计思路：
使用使用bmob 作为后台管理会员，使用“快发卡”平台卖激活码，试用一天，一天之后需要输入激活码才可以使用

