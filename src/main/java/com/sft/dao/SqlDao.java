package com.sft.dao;

import com.sft.model.SqlModel;
import com.sft.model.TypeModel;

import java.util.List;
import java.util.Map;

public interface SqlDao {

    public boolean addSql(SqlModel sqlModel);

    public boolean updateSql(SqlModel sqlModel);

    public List<TypeModel> getType();

    public int getCount(Map<String, String> whereMap);

    public List<SqlModel> getSql(Map<String, String> whereMap, int page, int pageSize);
}
