package com.logparser.userdao;

import com.logparser.usermodel.UserInfo;
import com.logparser.util.ConfigUtil;
import com.logparser.util.MongoUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserInfoDao {
  private static final String DB_NAME = ConfigUtil.getDbVideo();
  private static final String COL_NAME = "userbasicorder";
  private static final String QUERY_BY_IDS = "{\"userid\" : {\"$in\" : #}}";


  public static Map<String, String> getUserCity(String[] userids) {
    Iterator<UserInfo> infos = MongoUtil.getInstance().getIteratorByQuery(DB_NAME, COL_NAME,
        QUERY_BY_IDS, Integer.MAX_VALUE, UserInfo.class, new Object[]{userids});
    Map<String, String> res = new HashMap<String, String>();
    while (infos.hasNext()) {
      UserInfo info = infos.next();
      res.put(info.getUserid(), info.getCity());
    }
    return res;
  }
}
