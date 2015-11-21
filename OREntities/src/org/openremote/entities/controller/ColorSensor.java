package org.openremote.entities.controller;

import org.openremote.entities.panel.Color;

public class ColorSensor extends Sensor {
  private Color color;
  
  public ColorSensor() {
    super(SensorType.COLOR);
  }

  public Color getValue() {
    if (color == null) {
      String str = super.getStringValue();
      if (str != null) {
        str = str.replace("#", "");
        
        if (str.length() == 6) {
          try {
          color = new Color(
                  Integer.valueOf( str.substring( 1, 3 ), 16 ),
                  Integer.valueOf( str.substring( 3, 5 ), 16 ),
                  Integer.valueOf( str.substring( 5, 7 ), 16 ));
          } catch (NumberFormatException e) {            
          }
        }
      }
    }
    return color;
  }
  
  @Override
  protected void onValueChanged() {
    Color oldValue = color;
    color = null;
    raisePropertyChanged("value", oldValue, getValue());
  }
}
