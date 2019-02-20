package com.logparser.dao;

import com.logparser.logmodel.ResultLogModel;
import com.logparser.logmodel.TopResultLogModel;
import com.logparser.util.ConfigUtil;
import com.logparser.util.MongoUtil;

public class SecResultLogModelDao {
  private static final String DB_NAME = ConfigUtil.getDbServiceLog();
  private static final String COL_NAME = "SecResultLogCol";

  public static void addNewEntry(ResultLogModel model) {
    MongoUtil.getInstance().getCollection(DB_NAME, COL_NAME).insert(model);
  }
}
