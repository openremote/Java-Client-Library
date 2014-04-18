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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Forwards to screen or do other logical functions.
 * Includes to group, to screen, to previous screen , to next screen, back, login, logout and setting.
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class Navigation {
  public enum SystemScreenType {
    SETTINGS("setting"),
    BACK("back"),
    LOGIN("login"),
    LOGOUT("logout"),
    NEXT("next"),
    PREVIOUS("previous");
 
    private final String value;
    
    private SystemScreenType(String value) {
      this.value = value;
    }

    @JsonValue
    public String value() {
      return value;
    }

    public static SystemScreenType fromValue(String v) {
        for (SystemScreenType c: SystemScreenType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
  }

  @JsonBackReference("tabbaritem-navigation")
  TabBarItem parentTabBarItem;
  
  @JsonBackReference("gesture-navigation")
  Gesture parentGesture;
  
  @JsonBackReference("button-navigation")
  ButtonWidget parentButton;
  
  private SystemScreenType to;
  @JsonIgnore
  private Group toGroup;
  @JsonProperty("toGroup")
  private Integer toGroupId;
  @JsonIgnore
  private Screen toScreen;
  @JsonProperty("toScreen")
  private Integer toScreenId;
  
  public SystemScreenType getTo() {
    return to;
  }
  
  public Group getToGroup() {
    if (toGroupId != null && toGroup == null) {
      List<Group> groups = null;
      if (parentTabBarItem != null) {
        if (parentTabBarItem.parentTabBar.parentPanel != null) {
          groups = parentTabBarItem.parentTabBar.parentPanel.getGroups();
        } else if (parentTabBarItem.parentTabBar.parentGroup != null) {
          groups = parentTabBarItem.parentTabBar.parentGroup.parentGroupList.parentPanel.getGroups();
        }
      } else if (parentGesture != null) {
        groups = parentGesture.parentScreen.parentScreenList.parentPanel.getGroups();
      } else if (parentButton != null) {
        if (parentButton.parentAbsolute != null) {
          groups = parentButton.parentAbsolute.parentScreen.parentScreenList.parentPanel.getGroups();
        } else if (parentButton.parentCell != null) {
          groups = parentButton.parentCell.parentGrid.parentScreen.parentScreenList.parentPanel.getGroups();
        }
      }
      
      if (groups != null) {
        for (Group group : groups) {
          if (group.id == toGroupId) {
            toGroup = group;
            break;
          }
        }
      }
    }
    return toGroup;
  }
  
  public Screen getToScreen() {
    if (toScreenId != null && toScreen == null) {
      List<Screen> screens = null;
      if (parentTabBarItem != null) {
        if (parentTabBarItem.parentTabBar.parentPanel != null) {
          screens = parentTabBarItem.parentTabBar.parentPanel.getScreens();
        } else if (parentTabBarItem.parentTabBar.parentGroup != null) {
          screens = parentTabBarItem.parentTabBar.parentGroup.parentGroupList.parentPanel.getScreens();
        }
      } else if (parentGesture != null) {
        screens = parentGesture.parentScreen.parentScreenList.parentPanel.getScreens();
      } else if (parentButton != null) {
        if (parentButton.parentAbsolute != null) {
          screens = parentButton.parentAbsolute.parentScreen.parentScreenList.parentPanel.getScreens();
        } else if (parentButton.parentCell != null) {
          screens = parentButton.parentCell.parentGrid.parentScreen.parentScreenList.parentPanel.getScreens();
        }
      }
      
      if (screens != null) {
        for (Screen screen : screens) {
          if (screen.id == toScreenId) {
            toScreen = screen;
            break;
          }
        }
      }
    }
    return toScreen;
  }  
}
