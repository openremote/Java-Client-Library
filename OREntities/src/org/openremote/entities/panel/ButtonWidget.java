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
package org.openremote.entities.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.openremote.entities.controller.AsyncControllerCallback;
import org.openremote.entities.controller.CommandSender;
import org.openremote.entities.controller.ControlCommand;
import org.openremote.entities.controller.ControlCommandResponse;
import org.openremote.entities.controller.ControllerResponseCode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Button widget that sends a command on press (behaves differently depending on
 * the {@link ButtonWidget.Type}); simply change state to trigger the send
 * behaviour. TODO: Implement long press command functionality
 * <ul>
 * <li>
 * {@link Type#SHORTPRESS} = Send short press command once as soon as state
 * changes to {@link State#SHORT_PRESSED}</li>
 * <li>
 * {@link Type#SHORTPRESS_REPEATER} = Send short press command repeatedly as
 * soon as state changes to {@link State#SHORT_PRESSED} repeat period is
 * determined by {@link #getRepeatDelay}</li>
 * <li>
 * {@link Type#LONGPRESS} = Send long press command once after state has been
 * set to {@link State#LONG_PRESSED} for longer than {link #getLongPressDelay}</li>
 * <li>
 * {@link Type#LONGPRESS_REPEATER} = Send long press command repeatedly after
 * state has been set to {@link State#LONG_PRESSED} for longer than
 * {@link #getLongPressDelay} repeat period is determined by
 * {@link #getRepeatDelay}</li>
 * <li>
 * {@link Type#SHORT_AND_LONGPRESS} = Send short press command once and as soon
 * as state changes to {@link State#SHORT_PRESSED} then send long press command
 * after {@link #getLongPressDelay} delay has passed with changes state to
 * {@link State#LONG_PRESSED}</li>
 * </ul>
 * 
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
    SHORT_PRESSED,
    LONG_PRESSED,
    SHORT_RELEASED,
    LONG_RELEASED
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
  private Type buttonType = Type.SHORTPRESS;
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
   * Set the current state of this button; state can only be changed for buttons
   * that don't have navigation associated with them.
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
    onStateChanged(state == State.SHORT_PRESSED);
  }

  @JsonSetter("repeat")
  public void setIsRepeater(boolean isRepeater) {
    switch (getButtonType()) {
    case LONGPRESS:
      if (isRepeater) {
        setButtonType(Type.LONGPRESS_REPEATER);
      }
      break;
    case LONGPRESS_REPEATER:
      if (!isRepeater) {
        setButtonType(Type.LONGPRESS);
      }
      break;
    case SHORTPRESS:
      if (isRepeater) {
        setButtonType(Type.SHORTPRESS_REPEATER);
      }
      break;
    case SHORTPRESS_REPEATER:
      if (!isRepeater) {
        setButtonType(Type.SHORTPRESS);
      }
      break;
    default:
      break;
    }
  }

  public void setButtonType(Type buttonType) {
    if (this.buttonType == buttonType) {
      return;
    }
    Type oldValue = this.buttonType;
    this.buttonType = buttonType;
    raisePropertyChanged("buttonType", oldValue, buttonType);
  }

  public State getState() {
    return state;
  }

  public Navigation getNavigation() {
    return navigation;
  }

  public boolean hasControlCommand() {
    return hasControlCommand != null && hasControlCommand.booleanValue();
  }

  private String getDefaultImageName() {
    return defaultImageSrc != null ? defaultImageSrc.image.getSrc() : null;
  }

  private String getPressedImageName() {
    return pressedImageSrc != null ? pressedImageSrc.image.getSrc() : null;
  }

  public ResourceInfo getDefaultImage() {
    String imageName = getDefaultImageName();
    if (imageName != null && !imageName.isEmpty() && defaultImage == null) {
      defaultImage = new ResourceInfo(imageName, this);
    }

    return defaultImage;
  }

  public ResourceInfo getPressedImage() {
    String imageName = getPressedImageName();
    if (imageName != null && !imageName.isEmpty() && pressedImage == null) {
      pressedImage = new ResourceInfo(imageName, this);
    }

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

  @JsonGetter("repeat")
  public boolean isRepeater() {
    return buttonType == Type.LONGPRESS_REPEATER || buttonType == Type.SHORTPRESS_REPEATER;
  }

  private boolean isLongPress() {
    return buttonType == Type.LONGPRESS || buttonType == Type.LONGPRESS_REPEATER
            || buttonType == Type.SHORT_AND_LONGPRESS;
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
    if (commandSender != null) {
      commandSender.sendControlCommand(new ControlCommand(id, SHORT_PRESS_DATA),
              new AsyncControllerCallback<ControlCommandResponse>() {
                @Override
                public void onSuccess(ControlCommandResponse result) {
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
  public List<ResourceInfo> getResources() {
    List<ResourceInfo> resources = new ArrayList<ResourceInfo>();

    if (getDefaultImage() != null) {
      resources.add(getDefaultImage());
    }

    if (getPressedImage() != null) {
      resources.add(getPressedImage());
    }

    return resources;
  }

  @Override
  public void onResourceChanged(String name) {
    if (name.equals(getDefaultImageName())) {
      raisePropertyChanged("defaultImage", defaultImage, defaultImage);
    } else if (name.equals(getPressedImage())) {
      raisePropertyChanged("pressedImage", pressedImage, pressedImage);
    }
  }
}
