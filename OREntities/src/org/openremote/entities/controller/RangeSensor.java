package org.openremote.entities.controller;

public class RangeSensor extends Sensor {
  private Integer value;
  
  public RangeSensor() {
    super(SensorType.RANGE);
  }

  private int min;
  private int max;
  
  public int getMin() {
    return min;
  }
  public int getMax() {
    return max;
  }
  
  public int getValue() {
    if (value == null) {
      value = 0;
      try {
        value = Integer.parseInt(super.getStringValue());
        value = Math.min(getMax(), value);
        value = Math.max(getMin(), value);
      } catch(NumberFormatException e) {
        // Do nothing as this really shouldn't happen anyway
      }
    }
    return value;
  }
  
  @Override
  protected void onValueChanged() {
    int oldValue = value;
    value = null;
    raisePropertyChanged("value", oldValue, getValue());
  }
}
