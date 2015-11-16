package org.openremote.entities.panel;

import java.util.Date;

public class ResourceInfoDetails {
  private Date modifiedTime;
  private String contentType;
  
  public ResourceInfoDetails(Date modifiedTime, String contentType) {
    this.modifiedTime = modifiedTime;
    this.contentType = contentType;
  }
  
  public Date getModifiedTime() {
    return modifiedTime;
  }

  public String getContentType() {
    return contentType;
  }
}
