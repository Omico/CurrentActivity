package me.omico.currentactivity.model;

import io.realm.RealmObject;

/**
 * @author Omico 2017/9/30
 */

public class CurrentActivityData extends RealmObject {

    private String packageName;
    private String activityName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
