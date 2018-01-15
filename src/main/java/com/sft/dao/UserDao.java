package com.sft.dao;

import com.sft.model.AppUserModel;

public interface UserDao {

    public AppUserModel getUserInfoByPhone(String serverId, String phone);

    public AppUserModel getUserInfoByLoginId(String serverId, String login_id);

}
