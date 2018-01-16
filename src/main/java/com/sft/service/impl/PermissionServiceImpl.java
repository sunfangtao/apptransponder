package com.sft.service.impl;

import com.sft.dao.PermissionDao;
import com.sft.model.Permission;
import com.sft.service.PermissionService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class PermissionServiceImpl implements PermissionService {

    public static Map<String, Permission> urlMap = new TreeMap<String, Permission>();

    @Resource
    private PermissionDao permissionDao;

    public Permission getUrlByType(String serverId, String type) {
        synchronized (PermissionServiceImpl.class) {
            Permission permission = urlMap.get(serverId + "-" + type);
            if (permission != null) {
                return permission;
            }
        }
        Permission permission = permissionDao.getUrlByType(serverId, type);
        urlMap.put(serverId + "-" + type, permission);
        return permission;
    }

    public List<Permission> getPermissions(String serverId, String userId) {
        return permissionDao.getPermissions(serverId, userId);
    }

    public List<String> getRoles(String serverId, String userId) {
        return permissionDao.getRoles(serverId, userId);
    }

    public void update() {
        synchronized (PermissionServiceImpl.class) {
            urlMap.clear();
        }
    }
}
