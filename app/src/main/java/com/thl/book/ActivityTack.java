package com.thl.book;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity 栈管理
 */
public class ActivityTack {

    public List<Activity> activityList = new ArrayList<Activity>();

    public static ActivityTack tack = new ActivityTack();

    public static ActivityTack getInstanse() {
        return tack;
    }

    private ActivityTack() {}

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void exitLogin() {
        while (activityList.size() > 1) {
            activityList.get(activityList.size() - 1).finish();
        }
    }

    public Activity getFristActivity() {
        Activity mActivity = null;
        if (activityList != null && activityList.size() > 0) {
            mActivity = activityList.get(activityList.size() - 1);
        }
        return mActivity;
    }
}
