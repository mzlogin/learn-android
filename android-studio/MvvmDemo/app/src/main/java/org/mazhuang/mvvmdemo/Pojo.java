package org.mazhuang.mvvmdemo;

import lombok.Data;

/**
 * @author mzlogin
 * @date 2018/9/6
 */
@Data
public class Pojo {
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }
}
