package com.logparser.dao;

import com.logparser.logmodel.FeedbackLogModel;
import com.logparser.util.ConfigUtil;
import com.logparser.util.MongoUtil;

public class FeedbackLogDao {
  private static final String DB_NAME = ConfigUtil.getDbServiceLog();
  private static final String COL_NAME = "FeedbackLogCol";
  public static void addNewEntry(FeedbackLogModel model) {
    MongoUtil.getInstance().getCollection(DB_NAME, COL_NAME).insert(model);
  }
}
