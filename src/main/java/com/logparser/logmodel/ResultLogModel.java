package com.logparser.logmodel;


import java.io.Serializable;

public class ResultLogModel implements Serializable {
  private String time;
  private String cpid;
  private String city;
  private Long uv;
  private Long pv;
  private Long albumnum;

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

  public Long getUv() {
    return uv;
  }

  public void setUv(Long uv) {
    this.uv = uv;
  }

  public Long getPv() {
    return pv;
  }

  public void setPv(Long pv) {
    this.pv = pv;
  }

  public Long getAlbumnum() {
    return albumnum;
  }

  public void setAlbumnum(Long albumnum) {
    this.albumnum = albumnum;
  }
}
