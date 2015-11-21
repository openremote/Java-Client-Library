package org.openremote.entities.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Command {
  private int id;
  @JsonBackReference("device-command")
  @JsonIgnore()
  private Device device;
  private String name;
  List<String> tags;
  private String protocol;
  
  public int getId() {
    return id;
  }
  
  public Device getDevice() {
    return device;
  }
  
  public String getName() {
    return name;
  }
  
  public List<String> getTags() {
    return tags;
  }
  
  public String getProtocol() {
    return protocol;
  }  
}
