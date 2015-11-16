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

import java.util.ArrayList;
import java.util.List;

import org.openremote.entities.panel.version1.TabBar;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Group is parsed by group node, which contains id, name, screens and tabBar.
 *  
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class Group {
  @JsonBackReference("grouplist-group")
  GroupList parentGroupList;  
  int id;
  private String name;
  @JsonManagedReference("group-screeninclude")
  @JsonProperty("include")
  private List<ScreenInclude> screenIncludes;
  @JsonIgnore
  private List<Screen> screens;
  @JsonManagedReference("group-tabbar")
  @JsonProperty("tabbar")
  private TabBar tabBar;
 
  public String getName() {
    return name;
  }
  
  public List<Screen> getScreens() {
    if (screens == null) {
      List<Screen> screens = new ArrayList<Screen>();
      for (ScreenInclude include : screenIncludes) {
        int ref = include.screenRef;
        for (Screen screen : parentGroupList.parentPanel.getScreens()) {
          if (screen.id == ref) {
            screen.parentGroup = this;
            screens.add(screen);
          }
        }
      }
      this.screens = screens;
    }
    return screens;
  }
  
  public TabBar getTabBar() {
    return tabBar;
  }
}
