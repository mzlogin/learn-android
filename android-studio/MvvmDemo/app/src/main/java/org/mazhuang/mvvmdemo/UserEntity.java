package org.mazhuang.mvvmdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import lombok.Data;
import lombok.Setter;

import org.mazhuang.mvvmdemo.BR;

/**
 * @author mazhuang
 * @date 2018/8/5
 */
public class UserEntity extends BaseObservable {
    private String username;
    private String age;

    @Bindable
    public String getUsername() {
        return username;
    }

    @Bindable
    public String getAge() {
        return age;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    public void setAge(String age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }
}
