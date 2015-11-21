package org.openremote.entities.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.openremote.entities.panel.NotifyPropertyChanged;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(  
        use = JsonTypeInfo.Id.NAME,  
        include = JsonTypeInfo.As.PROPERTY,  
        property = "type")  
    @JsonSubTypes({  
        @JsonSubTypes.Type(value = CustomSensor.class, name = "custom"),  
        @JsonSubTypes.Type(value = RangeSensor.class, name = "range"),
        @JsonSubTypes.Type(value = LevelSensor.class, name = "level"),
        @JsonSubTypes.Type(value = SwitchSensor.class, name = "switch"),
        @JsonSubTypes.Type(value = ColorSensor.class, name = "color")}) 
public abstract class Sensor implements NotifyPropertyChanged {
  public static final String DEFAULT_STRING_VALUE = "N/A";
  private int id;
  @JsonBackReference("device-sensor")
  @JsonIgnore()
  private Device device;
  private String name;
  private SensorType type;
  private Command command;
  private int command_id;
  @JsonIgnore
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  @JsonIgnore
  private String strValue;
  
  protected Sensor(SensorType type) {
    this.type = type;
  }
  
  void raisePropertyChanged(String propertyName, Object oldValue, Object newValue) {
    pcs.firePropertyChange(propertyName, oldValue, newValue);
  }
  
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }
  
  public int getId() {
    return id;
  }
  
  public Device getDevice() {
    return device;
  }
  
  public String getName() {
    return name;
  }
  
  public SensorType getType() {
    return type;
  }
  
  public String getStringValue() {
    return strValue != null ? strValue : DEFAULT_STRING_VALUE;
  }
  
  public void setValue(String strValue) {
    if (this.strValue != null && this.strValue.equals(strValue)) {
      return;
    }    
    
    this.strValue = strValue;
    onValueChanged();
  }
  
  protected abstract void onValueChanged();
  
  Command getCommand() {
    if (command == null && device != null && device.getCommands() != null) {
      for (Command command : device.getCommands()) {
        if (command.getId() == command_id) {
          this.command = command;
          break;
        }
      }
    }
    return command;
  }
}
