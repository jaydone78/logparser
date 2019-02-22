package com.logparser.logmodel;

import java.io.Serializable;

public class ResultAlbumCntModel implements Serializable {
  private String time;
  private String cpid;
  private String city;
  private String albumid;
  private Long albumcnt;

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


  public String getAlbumid() {
    return albumid;
  }

  public void setAlbumid(String albumid) {
    this.albumid = albumid;
  }

  public Long getAlbumcnt() {
    return albumcnt;
  }

  public void setAlbumcnt(Long albumcnt) {
    this.albumcnt = albumcnt;
  }
}
