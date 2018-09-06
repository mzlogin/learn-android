package org.mazhuang.mvvmdemo;

import android.arch.lifecycle.MutableLiveData;

/**
 * @author mzlogin
 * @date 2018/9/6
 */
public class LiveDataPojo {
    public MutableLiveData<String> username = new MutableLiveData<>();

    public void setUsername(String username) {
        this.username.setValue(username);
    }
}
