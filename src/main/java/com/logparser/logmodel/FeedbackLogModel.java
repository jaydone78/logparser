package com.logparser.logmodel;

import java.io.Serializable;

public class FeedbackLogModel implements Serializable {
  private String time;
  private String cpid;
  private String city;
  private String bucketnum;
  private String type;
  private Long cnt;

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getCpid() {
    return cpid;
  }

  public void setCpid(String cpid) {
    this.cpid = cpid;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getBucketnum() {
    return bucketnum;
  }

  public void setBucketnum(String bucketnum) {
    this.bucketnum = bucketnum;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getCnt() {
    return cnt;
  }

  public void setCnt(Long cnt) {
    this.cnt = cnt;
  }
}
