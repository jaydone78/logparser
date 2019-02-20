package com.logparser.dao;

import com.logparser.logmodel.ResultAlbumCntModel;
import com.logparser.util.ConfigUtil;
import com.logparser.util.MongoUtil;

public class SecResultAlbumCntModelDao {
  private static final String DB_NAME = ConfigUtil.getDbServiceLog();
  private static final String COL_NAME = "SecResultAlbumCntCol";
  public static void addNewEntry(ResultAlbumCntModel model) {
    MongoUtil.getInstance().getCollection(DB_NAME, COL_NAME).insert(model);
  }
}
