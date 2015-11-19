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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The screen gesture, which contains 4 swipe types: "top-to-bottom", "bottom-to-top", "left-to-right" and "right-to-left".
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class Gesture {
  public enum GestureType {
    SWIPE_BOTTOM_TO_TOP("swipe-bottom-to-top"),
    SWIPE_TOP_TO_BOTTOM("swipe-top-to-bottom"),
    SWIPE_LEFT_TO_RIGHT("swipe-left-to-right"),
    SWIPE_RIGHT_TO_LEFT("swipe-right-to-left");

    private String value;
    
    private GestureType(String value) {
      this.value = value;
    }
    
    @JsonValue
    public String getValue() {
      return value;
    }
  }
  
  @JsonBackReference("screen-gesture")
  Screen parentScreen;  
  private int id;
  private GestureType type;
  @JsonProperty("navigate")
  private Navigation navigation;
  private Boolean hasControlCommand;
  
  public GestureType getType() {
    return type;
  }
  
  @JsonManagedReference("gesture-navigation")
  public Navigation getNavigation() {
    return navigation;
  }
  
  public Boolean getHasControlCommand() {
    return hasControlCommand;
  }  
}
