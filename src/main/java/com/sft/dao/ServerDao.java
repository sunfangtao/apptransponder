package com.sft.dao;

import com.sft.model.ServerModel;

public interface ServerDao {

    public boolean addServer(ServerModel serverModel);

    public boolean updateServer(ServerModel serverModel);
}
