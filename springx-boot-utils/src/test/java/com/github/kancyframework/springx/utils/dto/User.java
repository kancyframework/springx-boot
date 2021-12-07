package com.github.kancyframework.springx.utils.dto;

import java.io.Serializable;

/**
 * User
 *
 * @author huangchengkang
 * @date 2021/12/7 11:31
 */
public class User implements Serializable {

    private Long id;

    private String userName;

    private String password;

    private Integer age;

    private boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
}
