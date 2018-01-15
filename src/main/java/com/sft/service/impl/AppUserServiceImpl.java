package com.sft.service.impl;

import com.sft.dao.UserDao;
import com.sft.model.AppUserModel;
import com.sft.service.AppUserService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class AppUserServiceImpl implements AppUserService {

    @Resource
    private UserDao userDao;

    public AppUserModel getUserInfoByPhone(String serverId, String phone) {
        return userDao.getUserInfoByPhone(serverId, phone);
    }

    public AppUserModel getUserInfoByLoginId(String serverId, String login_id) {
        return userDao.getUserInfoByLoginId(serverId, login_id);
    }

}