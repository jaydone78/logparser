package com.logparser.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigUtil {

  private static Properties prop = new Properties();
  private static final String COMMON_PROP = "config.properties";
  private static final String DB_SERVICE_LOG = "db_service_log";
  private static final String DB_VIDEO = "db_video";
  private static final String RESULT_LOG_PATH = "result_log_path";
  private static final String FEEDBACK_LOG_PATH = "feedback_log_path";
  private static final String MONGO_HOST = "mongo_host";

  static {
    try {
      // load a properties file
      prop.load(ConfigUtil.class.getResourceAsStream("/" + COMMON_PROP));
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
    }
  }

  public static String getDbServiceLog() {
    return prop.getProperty(DB_SERVICE_LOG);
  }

  public static String getFeedbackLogPath() {
    return prop.getProperty(FEEDBACK_LOG_PATH);
  }

  public static String getDbVideo() {
    return prop.getProperty(DB_VIDEO);
  }

  public static String getResultLogPath() {
    return prop.getProperty(RESULT_LOG_PATH);
  }

  public static Map<String, String> getMongoHost() {
    String mongoStr = prop.getProperty(MONGO_HOST);
    String[] words = mongoStr.split(":");
    if (words.length != 2) {
      throw new IllegalArgumentException();
    }
    Map<String, String> res = new HashMap<String, String>();
    res.put(words[0], words[1]);
    return res;
  }


}
