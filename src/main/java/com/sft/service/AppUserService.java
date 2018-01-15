package com.sft.service;

import com.sft.model.AppUserModel;

public interface AppUserService {

    public AppUserModel getUserInfoByPhone(String serverId, String phone);

    public AppUserModel getUserInfoByLoginId(String serverId, String login_id);

}
