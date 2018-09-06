package org.mazhuang.mvvmdemo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * @author mzlogin
 * @date 2018/9/6
 */
public class ViewModelLiveData extends ViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>();

    public void setUsername(String username) {
        this.username.setValue(username);
    }
}
