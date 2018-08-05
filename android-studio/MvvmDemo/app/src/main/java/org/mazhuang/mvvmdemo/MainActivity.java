package org.mazhuang.mvvmdemo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.mazhuang.mvvmdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        UserEntity user = new UserEntity();
        user.setAge(30);
        user.setUsername("mzlogin");
        user.setNickname("mazhuang");

        activityMainBinding.setUser(user);

        user.setAge(18);

        UserEntity user2 = new UserEntity();
        user2.setUsername("ma");
        user2.setNickname("Jack");
        user2.setAge(50);

        activityMainBinding.setUser(user2);
    }
}
