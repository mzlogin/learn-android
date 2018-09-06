package org.mazhuang.mvvmdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * @author mazhuang
 * @date 2018/8/5
 */
public class ObservablePojo extends BaseObservable {
    private String username;

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }
}
