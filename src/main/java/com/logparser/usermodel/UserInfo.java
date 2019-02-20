package com.logparser.usermodel;

import java.io.Serializable;

public class UserInfo implements Serializable {
  private static final long serialVersionUID = -123452165652313L;
  private String city;
  private String userid;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }
}
