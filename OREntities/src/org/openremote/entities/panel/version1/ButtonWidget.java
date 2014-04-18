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

import java.util.Timer;
import java.util.TimerTask;

import org.openremote.entities.panel.CommandSender;
import org.openremote.entities.panel.CommandWidget;
import org.openremote.entities.panel.PanelCommand;
import org.openremote.entities.panel.PanelCommandResponse;
import org.openremote.entities.panel.ResourceInfo;
import org.openremote.entities.panel.ResourceLocator;
import org.openremote.entities.panel.ValueSetFailureHandler;
import org.openremote.entitites.controller.AsyncControllerCallback;
import org.openremote.entitites.controller.ControllerResponseCode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Button widget that sends a command on press (behaves differently depending
 * on the {@link Type}); simply change state to trigger the send behaviour.
 * TODO: Implement long press command functionality
 * <ul>
 * <li>
 * {@value Type#SHORTPRESS} = Send short press command once as soon as state changes to {@value State#PRESSED}
 * </li>
 * <li>
 * {@value Type#SHORTPRESS_REPEATER} = Send short press command repeatedly as soon as state changes to {@value State#PRESSED}
 * repeat period is determined by {@value #repeatDelay}
 * </li>
 * <li>
 * {@value Type#LONGPRESS} = Send long press command once after state has been set to {@value State#PRESSED} for
 * longer than {@value #longPressDelay}
 * </li>
 * <li>
 * {@value Type#LONGPRESS_REPEATER} = Send long press command repeatedly after state has been set to {@value State#PRESSED} for
 * longer than {@value #longPressDelay}
 * repeat period is determined by {@value #repeatDelay}
 * </li>
 * <li>
 * {@value Type#SHORT_AND_LONGPRESS} = Send short press command once as soon as state changes to {@value State#PRESSED} then
 * send long press command after state has been set to {@value State#PRESSED} for longer than {@value #longPressDelay}
 * </li>
 * </ul>
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class ButtonWidget extends Widget implements CommandWidget {
  public enum Type {
    SHORTPRESS,
    SHORTPRESS_REPEATER,
    LONGPRESS,
    LONGPRESS_REPEATER,
    SHORT_AND_LONGPRESS
  }
  
  public enum State {
    RELEASED,
    PRESSED
  }

  @JsonBackReference("cell-button")
  CellLayout parentCell;
  @JsonBackReference("absolute-button")
  AbsoluteLayout parentAbsolute;
  
  @JsonProperty("navigate")
  @JsonManagedReference("button-navigation")
  private Navigation navigation;
  private Boolean hasControlCommand;
  @JsonProperty("default")
  private ImageSrcWrapper defaultImageSrc;
  @JsonProperty("pressed")
  private ImageSrcWrapper pressedImageSrc;
  @JsonIgnore
  public static final int MIN_REPEAT_DELAY = 100;
  @JsonIgnore
  public static final int MIN_LONG_PRESS_DELAY = 250;
  @JsonIgnore
  private static final String SHORT_PRESS_DATA = "click";
  @JsonIgnore
  private Timer timer;
  @JsonIgnore
  private TimerTask timerTask;
  @JsonIgnore
  private int repeatDelay;
  @JsonIgnore
  private int longPressDelay;
  @JsonIgnore
  private State state;
  @JsonIgnore
  private Type buttonType;
  @JsonIgnore
  private CommandSender commandSender;
  @JsonIgnore
  private ValueSetFailureHandler commandFailureHandler;
  @JsonIgnore
  private ResourceInfo defaultImage;
  @JsonIgnore
  private ResourceInfo pressedImage;
  
  // GETTERS AND SETTERS
  /**
   * Set the current state of this button; state can only
   * be changed for buttons that don't have navigation
   * associated with them.
   * 
   * @param state
   */
  public void setState(State state) {
    // Return if value hasn't changed or there is
    // navigation associated with this button
    if (navigation != null || this.state == state) {
      return;
    }
    
    this.state = state;
    
    // Start or stop timer
    onStateChanged(state == State.PRESSED);   
  }

  public State getState() {
    return state;
  }

  public Navigation getNavigation() {
    return navigation;
  }
  
  public Boolean getHasControlCommand() {
    return hasControlCommand;
  }

  private String getDefaultImageName() {
    return defaultImageSrc != null ? defaultImageSrc.image.getSrc() : null;
  }

  private String getPressedImageName() {
    return pressedImageSrc != null ? pressedImageSrc.image.getSrc() : null;
  }
  
  public ResourceInfo getDefaultImage() {
    return defaultImage;
  }
  
  private void setDefaultImage(ResourceInfo defaultImage) {
    if (this.defaultImage == defaultImage) {
      return;
    }
    ResourceInfo oldValue = this.defaultImage;
    
    this.defaultImage = defaultImage;
    raisePropertyChanged("defaultImage", oldValue, defaultImage);
  }
  
  private void setPressedImage(ResourceInfo pressedImage) {
    if (this.pressedImage == pressedImage) {
      return;
    }
    ResourceInfo oldValue = this.pressedImage;
    
    this.pressedImage = pressedImage;
    raisePropertyChanged("pressedImage", oldValue, pressedImage);
  }
  
  public ResourceInfo getPressedImage() {
    return pressedImage;
  }
  
  public int getRepeatDelay() {
    return repeatDelay;
  }
  
  public int getLongPressDelay() {
    return longPressDelay > MIN_LONG_PRESS_DELAY ? longPressDelay : MIN_LONG_PRESS_DELAY;
  }
  
  public Type getButtonType() {
    return buttonType;
  }

  // HELPERS  
  private boolean isRepeater() {
    return buttonType == Type.LONGPRESS_REPEATER || buttonType == Type.SHORTPRESS_REPEATER;
  }
  
  private boolean isLongPress() {
    return buttonType == Type.LONGPRESS || buttonType == Type.LONGPRESS_REPEATER || buttonType == Type.SHORT_AND_LONGPRESS;
  }
  
  private void onStateChanged(boolean isPressed) {
    if (!isPressed) {
      if (timer != null) {
        timer.cancel();
        return;
      }
    }
    
    // Fire command straight away if only short press
    if (!isLongPress()) {
      sendCommand(false);
    }

    if (isLongPress() || isRepeater()) {
      timer = new Timer();
      int initialWait = isLongPress() ? getLongPressDelay() : getRepeatDelay();
      
      if (timerTask == null) {
        timerTask = new TimerTask() {
          @Override
          public void run() {
            sendCommand(isLongPress());
          }
        }; 
      }
      
      timer.schedule(timerTask, initialWait, getRepeatDelay());
    }   
  }
  
  // TODO: Add support for long press commands
  private void sendCommand(boolean isLongPress) {
    if (commandSender != null)
    {
      commandSender.sendCommand(new PanelCommand(id, SHORT_PRESS_DATA), new AsyncControllerCallback<PanelCommandResponse>() {
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
  
  // OVERRIDES
  @Override
  public void setCommandSender(CommandSender commandSender) {
    this.commandSender = commandSender;
  }

  @Override
  public void setValueFailureHandler(ValueSetFailureHandler commandFailureHandler) {
    this.commandFailureHandler = commandFailureHandler;
  }

  @Override
  protected void OnResourceLocatorChanged(ResourceLocator resourceLocator) {
    String defaultImage = getDefaultImageName();
    if (defaultImage != null && !defaultImage.isEmpty()) {
      resolveResource(defaultImage, false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setDefaultImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setDefaultImage(null);
        }
      });
    }
    
    String pressedImage = getPressedImageName();
    if (pressedImage != null && !pressedImage.isEmpty()) {
      resolveResource(pressedImage, false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setPressedImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setPressedImage(null);
        }
      });
    }
  }

//  @Override
//  protected String[] getAllResourceNames() {
//    return new String[] {
//            getDefaultImageName(),
//            getPressedImageName()
//    };
//  }
}
