package org.mazhuang.mvvmdemo;

import android.arch.lifecycle.ViewModel;

/**
 * @author mzlogin
 * @date 2018/9/6
 */
public class ViewModelPojo extends ViewModel {
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
