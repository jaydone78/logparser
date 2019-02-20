package com.logparser.dao;

import com.logparser.logmodel.TopResultAlbumCntModel;
import com.logparser.util.ConfigUtil;
import com.logparser.util.MongoUtil;

public class TopResultAlbumCntModelDao {
  private static final String DB_NAME = ConfigUtil.getDbServiceLog();
  private static final String COL_NAME = "TopResultAlbumCntCol";
  public static void addNewEntry(TopResultAlbumCntModel model) {
    MongoUtil.getInstance().getCollection(DB_NAME, COL_NAME).insert(model);
  }
}
