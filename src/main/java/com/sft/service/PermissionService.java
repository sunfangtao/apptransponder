package com.sft.service;

import com.sft.model.Permission;

import java.util.List;

public interface PermissionService {

    public Permission getUrlByType(String serverId, String type);

    public List<Permission> getPermissions(String serverId, String userId);

    public List<String> getRoles(String serverId, String userId);

    public void update();
}
