package org.openremote.entities.controller;

import org.openremote.entities.panel.SwitchState;

public class SwitchSensor extends Sensor {
  SwitchState value;
  
  public SwitchSensor() {
    super(SensorType.SWITCH);
  }

  public SwitchState getValue() {
    if (value == null) {
      boolean bValue = Boolean.parseBoolean(super.getStringValue());
      value = bValue ? SwitchState.ON :SwitchState.OFF;
    }
    return value;
  }
  
  @Override
  protected void onValueChanged() {
    SwitchState oldValue = value;
    value = null;
    raisePropertyChanged("value", oldValue, getValue());
  }
}
