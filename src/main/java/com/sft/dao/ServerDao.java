package com.sft.dao;

import com.sft.model.ServerModel;

import java.util.List;
import java.util.Map;

public interface ServerDao {

    public boolean addServer(ServerModel serverModel);

    public boolean updateServer(ServerModel serverModel);

    public int getCount(Map<String, String> whereMap);

    public List<ServerModel> getSever(Map<String, String> whereMap, int page, int pageSize);
}
