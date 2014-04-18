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

import org.openremote.entities.panel.CommandSender;
import org.openremote.entities.panel.CommandWidget;
import org.openremote.entities.panel.PanelCommand;
import org.openremote.entities.panel.PanelCommandResponse;
import org.openremote.entities.panel.ResourceInfo;
import org.openremote.entities.panel.ResourceLocator;
import org.openremote.entities.panel.ValueSetFailureHandler;
import org.openremote.entitites.controller.AsyncControllerCallback;
import org.openremote.entitites.controller.ControllerResponseCode;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Switch widget is capable of sending an alternating on/off command and is
 * capable of feeding back its' current state based on a sensor value.   
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class SwitchWidget extends SensoryWidget implements CommandWidget {
  public enum State {
    ON,
    OFF
  }
  
  @JsonIgnore  
  private State state;
  @JsonIgnore
  private CommandSender commandSender;
  @JsonIgnore
  private ValueSetFailureHandler commandFailureHandler;
  @JsonIgnore
  private ResourceInfo offImage;
  @JsonIgnore
  private ResourceInfo onImage;
  @JsonIgnore
  private boolean imageNamesFound;
  @JsonIgnore
  private String offImageName;
  @JsonIgnore
  private String onImageName;
  
  public ResourceInfo getOffImage() {
    return offImage;
  }

  private void setOffImage(ResourceInfo offImage) {
    this.offImage = offImage;
    raisePropertyChanged("offImage", null, offImage);
  }

  public ResourceInfo getOnImage() {
    return onImage;
  }

  private void setOnImage(ResourceInfo onImage) {
    this.onImage = onImage;
    raisePropertyChanged("onImage", null, onImage);
  }

  private String getOffImageName() {
    if (!imageNamesFound) {
      getImageNames();
    }
    return offImageName;
  }
  
  public String getOnImageName() {
    if (!imageNamesFound) {
      getImageNames();
    }
    
    return onImageName;
  }
  
  private synchronized void getImageNames() {
    SensorLink link = getSensorLinks() != null && !getSensorLinks().isEmpty() ? getSensorLinks().get(0) : null;
    if (link != null) {
      for (StateMap map : link.getStates()) {
        if (map.getName().equalsIgnoreCase("on")) {
          onImageName = map.getValue();
        } else if (map.getName().equalsIgnoreCase("off")) {
          offImageName = map.getValue();
        }
      }
    }
    imageNamesFound = true;
  }
  
  @Override
  public void setCommandSender(CommandSender commandSender) {
    this.commandSender = commandSender;
  }
  
  @Override
  public void setValueFailureHandler(ValueSetFailureHandler commandFailureHandler) {
    this.commandFailureHandler = commandFailureHandler;
  }
  
  @Override
  public void onSensorValueChanged(int sensorId, String value) {
    try {
      State state = Enum.valueOf(State.class, value.toUpperCase());
      if (this.state == state) {
        return;
      }
      
      State oldValue = this.state;
      this.state = state;      
      raisePropertyChanged("state", oldValue, state);
    } catch (IllegalArgumentException e) {
      // Do nothing as this shouldn't happen
    } catch (NullPointerException e) {
      // Do nothing as this shouldn't happen      
    }    
  }
  
  public State getState() {
    return state;
  }
  
  public void setState(State state) {
    if (this.state == state) {
      return;
    }

    // Wait for on sensor changed callback to actually change the state
    
    if (commandSender != null)
    {
      String data = state.toString().toLowerCase();
      
      commandSender.sendCommand(new PanelCommand(id, data), new AsyncControllerCallback<PanelCommandResponse>() {
        @Override
        public void onSuccess(PanelCommandResponse result) {
          // Do nothing here
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          if (commandFailureHandler != null) {
            commandFailureHandler.onSetValueFailed("state", error);
          }
        }
      });
    }
  }

  @Override
  protected void OnResourceLocatorChanged(ResourceLocator resourceLocator) {
    // Get on and off images
    if (getOnImageName() != null && !getOnImageName().isEmpty()) {
      resolveResource(getOnImageName(), false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setOnImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setOnImage(null);
        }
      });
    }
    
    if (getOffImageName() != null && !getOffImageName().isEmpty()) {
      resolveResource( getOffImageName(), false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setOffImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setOffImage(null);
        }
      });
    }
  }

//  @Override
//  protected String[] getAllResourceNames() {
//    return new String[] { getOnImageName(), getOffImageName() };
//  }
}
