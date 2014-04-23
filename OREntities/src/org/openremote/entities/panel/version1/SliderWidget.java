/*
 * OpenRemote, the Home of the Digital Home.
 * Copyright 2008-2014, OpenRemote Inc.
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openremote.entities.panel.version1;

import org.openremote.entities.controller.AsyncControllerCallback;
import org.openremote.entities.controller.ControllerResponseCode;
import org.openremote.entities.panel.CommandSender;
import org.openremote.entities.panel.CommandWidget;
import org.openremote.entities.panel.PanelCommand;
import org.openremote.entities.panel.PanelCommandResponse;
import org.openremote.entities.panel.ResourceInfo;
import org.openremote.entities.panel.ResourceLocator;
import org.openremote.entities.panel.ValueSetFailureHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Slider widget for displaying an integer value on an interactive or passive
 * slider.  
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class SliderWidget extends SensoryWidget implements CommandWidget {
  public enum State {
    RELEASED,
    PRESSED
  }
  
  @JsonProperty("thumbImage")
  private String thumbImageName;
  @JsonIgnore
  private ResourceInfo thumbImage;
  @JsonIgnore
  private ResourceInfo minTrackImage;
  @JsonIgnore
  private ResourceInfo maxTrackImage;
  @JsonIgnore
  private ResourceInfo minImage;
  @JsonIgnore
  private ResourceInfo maxImage;
  private boolean vertical;
  private boolean passive;
  private SliderMinMax min;
  private SliderMinMax max;
  @JsonIgnore
  private int value;
  @JsonIgnore
  private CommandSender commandSender;
  @JsonIgnore
  private ValueSetFailureHandler commandFailureHandler;
  @JsonIgnore
  private State minImageState;
  @JsonIgnore
  private State maxImageState;
  
  public State getMinImageState() {
    return minImageState;
  }

  public void setMinImageState(State minImageState) {
    // Return if value hasn't changed or there is
    // navigation associated with this button
    if (this.minImageState == minImageState) {
      return;
    }
    
    this.minImageState = minImageState;
        
    // Just decrement value by one for now when pressed
    if (minImageState == State.PRESSED) {
      setValue(value-1);
    }
  }

  public State getMaxImageState() {
    return maxImageState;
  }

  public void setMaxImageState(State maxImageState) {
    // Return if value hasn't changed or there is
    // navigation associated with this button
    if (this.maxImageState == maxImageState) {
      return;
    }
    
    this.maxImageState = maxImageState;
        
    // Just increment value by one for now when pressed
    if (maxImageState == State.PRESSED) {
      setValue(value+1);
    }
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    value = value < getMinValue() ? getMinValue() : value > getMaxValue() ? getMaxValue() : value;
    
    if (isPassive() || this.value == value)
    {
      return;
    }
    
    this.value = value;
    
    if (commandSender != null)
    {
      commandSender.sendCommand(new PanelCommand(id, Integer.toString(getValue())), new AsyncControllerCallback<PanelCommandResponse>() {
        @Override
        public void onSuccess(PanelCommandResponse result) {
          // Do nothing here
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          if (commandFailureHandler != null) {
            commandFailureHandler.onSetValueFailed("value", error);
          }
        }
      });
    }
  }
  
  String getThumbImageName() {
      return thumbImageName;
  }
    
  String getMinImageName() {
    return min != null ? min.getImage() : null;
  }
  
  String getMaxImageName() {
    return max != null ? max.getImage() : null;
  }
  
  String getMinTrackImageName() {
    return min != null ? min.getTrackImage() : null;
  }
  
  String getMaxTrackImageName() {
    return max != null ? max.getTrackImage() : null;
  }
  
  public ResourceInfo getThumbImage() {
    return thumbImage;
  }
  
  public ResourceInfo getMinImage() {
    return minImage;
  }
  
  public ResourceInfo getMaxImageData() {
    return maxImage;
  }
  
  public ResourceInfo getMinTrackImage() {
    return minTrackImage;
  }
  
  public ResourceInfo getMaxTrackImage() {
    return maxTrackImage;
  }
  
  public int getMinValue() {
    return min != null ? min.getValue() : 0;
  }
  
  public int getMaxValue() {
    return max != null ? max.getValue() : 0;
  }
  
  public boolean isVertical() {
    return vertical;
  }
  
  public boolean isPassive() {
    return passive;
  }

  private void setThumbImage(ResourceInfo data) {
    this.thumbImage = data;
    raisePropertyChanged("thumbImage", null, data);
  }
  
  private void setMinImage(ResourceInfo data) {
    this.minImage = data;
    raisePropertyChanged("minImage", null, data);
  }
  
  private void setMaxImage(ResourceInfo data) {
    this.maxImage = data;
    raisePropertyChanged("maxImage", null, data);
  }
  
  private void setMinTrackImage(ResourceInfo data) {
    this.minTrackImage = data;
    raisePropertyChanged("minTrackImage", null, data);
  }
  
  private void setMaxTrackImage(ResourceInfo data) {
    this.maxTrackImage = data;
    raisePropertyChanged("maxTrackImage", null, data);
  }
  
  @Override
  public void setCommandSender(CommandSender commandSender) {
    if (!isPassive()) {
      this.commandSender = commandSender;
    }
  }

  @Override
  public void onSensorValueChanged(int sensorId, String value) {
    // At present sensor can only relate to the value so check value is int
    try {
      int val = Integer.parseInt(value);
      int oldValue = this.value;
      this.value = val;
      raisePropertyChanged("value", oldValue, val);
    } catch(NumberFormatException e) {
      // Do nothing as this really shouldn't happen anyway
    }
  }
  
  @Override
  public void setValueFailureHandler(ValueSetFailureHandler commandFailureHandler) {
    this.commandFailureHandler = commandFailureHandler;
  }

  @Override
  protected void OnResourceLocatorChanged(ResourceLocator resourceLocator) {
    // Try and resolve any images that have been set
    if (thumbImageName != null && !thumbImageName.isEmpty()) {
      resolveResource(thumbImageName, false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setThumbImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setThumbImage(null);
        }
      });
    }
    
    String minImage = getMinImageName();    
    if (minImage != null && !minImage.isEmpty()) {
      resolveResource(minImage, false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setMinImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setMinImage(null);
        }
      });
    }
    
    String maxImage = getMaxImageName();    
    if (maxImage != null && !maxImage.isEmpty()) {
      resolveResource(maxImage, false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setMaxImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setMaxImage(null);
        }
      });
    }
    
    String minTrackImage = getMinTrackImageName();    
    if (minTrackImage != null && !minTrackImage.isEmpty()) {
      resolveResource(minTrackImage, false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setMinTrackImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setMinTrackImage(null);
        }
      });
    }
    
    String maxTrackImage = getMaxTrackImageName();    
    if (maxTrackImage != null && !maxTrackImage.isEmpty()) {
      resolveResource(maxTrackImage, false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setMaxTrackImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setMaxTrackImage(null);
        }
      });
    }
  }

//  @Override
//  protected String[] getAllResourceNames() {
//    return new String[] {
//      getThumbImageName(),
//      getMinImageName(),
//      getMinTrackImageName(),
//      getMaxImageName(),
//      getMaxTrackImageName()
//    };
//  }
}
