package com.logparser.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

public class TimeUtil {
  public static String getPre1HourTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
    return sdf.format(DateUtils.addHours(new Date(), -1));
  }

  public static void main(String[] args) {
    System.out.println(getPre1HourTime());
  }
}
