package com.sft.dao;

import com.sft.model.SqlModel;
import com.sft.model.TypeModel;
import com.sft.model.VersionModel;

import java.util.List;
import java.util.Map;

public interface VersionDao {

    public boolean addVersion(VersionModel versionModel);

    public boolean updateVersion(VersionModel versionModel);

    public int getCount(Map<String, String> whereMap);

    public List<VersionModel> getVersion(Map<String, String> whereMap, int page, int pageSize);

    public VersionModel getVersionByServerId(String serverId);
}
