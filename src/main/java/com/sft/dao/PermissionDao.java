package com.sft.dao;

import com.sft.model.Permission;

import java.util.List;

public interface PermissionDao {

    /**
     * 获取对应的url
     *
     * @param type
     * @return
     */
    public Permission getUrlByType(String serverId, String type);

    public List<Permission> getPermissions(String serverId, String userId);

    public List<String> getRoles(String serverId, String userId);
}
