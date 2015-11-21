/*
 * OpenRemote, the Home of the Digital Home.
 * Copyright 2008-2015, OpenRemote Inc.
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
package org.openremote.entities.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openremote.entities.controller.AsyncControllerCallback;
import org.openremote.entities.controller.CommandSender;
import org.openremote.entities.controller.ControlCommand;
import org.openremote.entities.controller.ControlCommandResponse;
import org.openremote.entities.controller.ControllerResponseCode;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Color Picker widget for allowing user to interactively select a color to be pushed to a sensor.  
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class ColorPickerWidget extends Widget implements CommandWidget {
  @JsonIgnore
  private CommandSender commandSender;
  @JsonIgnore
  private ValueSetFailureHandler commandFailureHandler;
  private ImageResource image;
  @JsonIgnore
  private ResourceInfo imageResource;
  
  @Override
  public void setCommandSender(CommandSender commandSender) {
    this.commandSender = commandSender;    
  }
  
  @Override
  public void setValueFailureHandler(ValueSetFailureHandler commandFailureHandler) {
    this.commandFailureHandler = commandFailureHandler;
  }
  
  public void setValue(Color color) {
    if (commandSender != null)
    {
      commandSender.sendControlCommand(new ControlCommand(id, color.toString()), new AsyncControllerCallback<ControlCommandResponse>() {
        @Override
        public void onSuccess(ControlCommandResponse result) {
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
  
  private String getImageName() {
    return image != null ? image.getSrc() : null;
  }

  public ResourceInfo getImage() {
    String imageName = getImageName();
    if (imageName != null && !imageName.isEmpty() && imageResource == null) {
      imageResource = new ResourceInfo(imageName, this);
    }
    
    return imageResource;
  }

  @Override
  public List<ResourceInfo> getResources() {
    return Arrays.asList(new ResourceInfo[] {getImage()});
  }

  @Override
  public void onResourceChanged(String name) {
    if (name.equals(getImageName())) {
      raisePropertyChanged("image", imageResource, imageResource);
    }
  }

}
