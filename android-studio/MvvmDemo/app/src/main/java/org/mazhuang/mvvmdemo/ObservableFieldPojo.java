package org.mazhuang.mvvmdemo;

import android.databinding.ObservableField;

/**
 * @author mzlogin
 * @date 2018/9/6
 */
public class ObservableFieldPojo {
    public ObservableField<String> username = new ObservableField<>();

    public void setUsername(String username) {
        this.username.set(username);
    }
}
