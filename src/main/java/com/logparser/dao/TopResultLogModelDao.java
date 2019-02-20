package com.logparser.dao;

import com.logparser.logmodel.TopResultLogModel;
import com.logparser.util.ConfigUtil;
import com.logparser.util.MongoUtil;

public class TopResultLogModelDao {
  private static final String DB_NAME = ConfigUtil.getDbServiceLog();
  private static final String COL_NAME = "TopResultLogCol";

  public static void addNewEntry(TopResultLogModel model) {
    MongoUtil.getInstance().getCollection(DB_NAME, COL_NAME).insert(model);
  }
}
