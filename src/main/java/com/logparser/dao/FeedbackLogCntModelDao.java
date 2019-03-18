package com.logparser.dao;

import com.logparser.logmodel.FeedBackLogCntModel;
import com.logparser.util.ConfigUtil;
import com.logparser.util.MongoUtil;

public class FeedbackLogCntModelDao {
  private static final String DB_NAME = ConfigUtil.getDbServiceLog();
  private static final String COL_NAME = "FeedbackLogCntCol";
  public static void addNewEntry(FeedBackLogCntModel model) {
    MongoUtil.getInstance().getCollection(DB_NAME, COL_NAME).insert(model);
  }
}
