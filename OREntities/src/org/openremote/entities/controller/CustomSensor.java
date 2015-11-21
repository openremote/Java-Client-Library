package org.openremote.entities.controller;

import java.util.List;

public class CustomSensor extends Sensor {
  private String value;
  List<String> states;
  
  public CustomSensor() {
    super(SensorType.CUSTOM);
  }
  
  public List<String> getStates() {
    return states;
  }

  public String getValue() {
    if (value == null) {
      value = super.getStringValue();
    }
    
    return value;
  }
  
  @Override
  protected void onValueChanged() {
    String oldValue = value;
    value = null;
    raisePropertyChanged("value", oldValue, getValue());
  }
}
