package org.openremote.entities.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DeviceInfo {
  @JsonIgnore
  private int id;
  private String name;
  
  public String getName() {
    return name;
  }
}
