package com.logparser.util;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

public class MongoUtil {

  private Jongo dataSourceServiceLog = null;
  private Jongo dataSourceVideo= null;


  protected static MongoUtil instance = null;

  protected MongoUtil() {
    this.setUpContext();
  }

  public static MongoUtil getInstance() {

    if (instance == null) {
      synchronized (MongoUtil.class) {
        instance = new MongoUtil();
      }
    }
    return instance;
  }

  private void setUpContext() {
    Map<String, String> hostPort = ConfigUtil.getMongoHost();
    String host = "";
    String port = "";
    for (Entry<String, String> entry : hostPort.entrySet()) {
      host = entry.getKey();
      port = entry.getValue();
      break;
    }
    MongoClientOptions options = MongoClientOptions.builder()
        .connectionsPerHost(200)
        .threadsAllowedToBlockForConnectionMultiplier(5).build();

    ServerAddress address = new ServerAddress(host, Integer.parseInt(port));

    MongoClient clientServiceLog = new MongoClient(address, options);
    DB dbServiceLog = clientServiceLog.getDB(ConfigUtil.getDbServiceLog());
    dataSourceServiceLog = new Jongo(dbServiceLog);

    MongoClient clientVideo = new MongoClient(address, options);
    DB dbVideo = clientVideo.getDB(ConfigUtil.getDbVideo());
    dataSourceVideo = new Jongo(dbVideo);
  }

  private Jongo getDB(String name) {
    if (name.equals(ConfigUtil.getDbServiceLog()))
      return dataSourceServiceLog;
    else if (name.equals(ConfigUtil.getDbVideo()))
      return dataSourceVideo;
    return null;
  }

  public MongoCollection getCollection(String dbname, String name) {
    return getDB(dbname).getCollection(name);
  }

  public <T extends Serializable> Iterator<T> getIteratorByQuery(String dbName,
      String collectionName, String query,
      int limit, Class<T> clazz, Object... params) {
    MongoCollection collection = getDB(dbName).getCollection(collectionName);

    Iterator<T> iter;
    if (query.length() != 0) {
      iter = collection.find(query, params).limit(limit).as(clazz).iterator();
    } else {
      iter = collection.find().limit(limit).as(clazz).iterator();
    }

    return iter;
  }
}
