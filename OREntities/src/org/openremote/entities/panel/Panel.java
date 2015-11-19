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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains the entire definition of a panel 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public final class Panel {
  @JsonManagedReference("panel-grouplist")
  @JsonProperty("groups")
  private GroupList groupsList;
  @JsonManagedReference("panel-screenlist")
  @JsonProperty("screens")
  private ScreenList screenList;
  @JsonManagedReference("panel-tabbar")
  @JsonProperty("tabbar")
  private TabBar tabBar;
  
  public List<Group> getGroups() {
    return groupsList != null ? groupsList.groups : null;
  }

  public List<Screen> getScreens() {
    return screenList != null ? screenList.screens : null;
  }

  public TabBar getTabBar() {
    return tabBar;
  }
  
  public List<Widget> getWidgets() {
    return getWidgets(new Class[] {Widget.class});
  }
  
  public List<Widget> getWidgets(Class<?>[] widgetTypes) {
    List<Widget> widgets = new ArrayList<Widget>();
    for (Screen screen : screenList.screens) {
      widgets.addAll(screen.getWidgets(widgetTypes));
    }
    
    return widgets;
  }
  
  public List<ResourceConsumer> getResourceConsumers() {
    List<ResourceConsumer> consumers = new ArrayList<ResourceConsumer>();
    for (Screen screen : getScreens()) {
      if (screen.getBackground() != null) {
        consumers.add(screen.getBackground());
      }
    }
    if (getTabBar() != null) {
      for (TabBarItem item : getTabBar().getItems()) {
        consumers.add(item);
      }
    }
    for (Widget widget : getWidgets()) {
      consumers.add(widget);
    }
    return consumers;
  }
  
  @SuppressWarnings("unchecked")
  public <T extends Widget> List<T> getWidgets(Class<T> type) {
    List<Widget> widgets = new ArrayList<Widget>();
    for (Screen screen : screenList.screens) {
      widgets.addAll(screen.getWidgets(new Class<?>[] {type}));
    }
    return (List<T>)widgets;
  }
}
