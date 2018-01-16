package com.sft.dao;

import com.sft.model.AppUserModel;

public interface UserDao {
    public AppUserModel getUserInfo(String serverId, String phone);
}
