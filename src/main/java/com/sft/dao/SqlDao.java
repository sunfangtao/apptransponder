package com.sft.dao;

import com.sft.model.SqlModel;

public interface SqlDao {

    public boolean addSql(SqlModel sqlModel);

    public boolean updateSql(SqlModel sqlModel);
}
