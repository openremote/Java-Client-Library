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

import org.openremote.entities.panel.ScreenBackground;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Screen object that contains layouts and supports gestures
 *  
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class Screen {
  @JsonBackReference("screenlist-screen")
  ScreenList parentScreenList;
  @JsonBackReference("group-screen")
  Group parentGroup;
  int id;
  private String name;
  @JsonProperty("absolute")
  @JsonManagedReference("screen-absolute")
  private List<AbsoluteLayout> absoluteLayouts;
  @JsonProperty("grid")
  @JsonManagedReference("screen-grid")
  private List<GridLayout> gridLayouts;
  @JsonProperty("gesture")
  @JsonManagedReference("screen-gesture")
  private List<Gesture> gestures;
  private ScreenBackground background;
  private Integer inverseScreenId;
  private Boolean landscape;
  @JsonIgnore
  private Screen inverseScreen;
  
  public String getName() {
    return name;
  }
    
  @SuppressWarnings("unchecked")
  public <T extends Widget> List<T> getWidgets(Class<T> type) {
    return (List<T>)getWidgets(new Class<?>[] {type});
  }
  
  public List<Widget> getWidgets() {
    return getWidgets(new Class[] {Widget.class});
  }
  public List<Widget> getWidgets(Class<?>[] widgetTypes) {
    List<Widget> widgets = new ArrayList<Widget>();
    if (absoluteLayouts != null) {
      for (AbsoluteLayout abs : absoluteLayouts) {
        Widget widget = abs.getWidget();
        boolean typeMatch = false;
        for (Class<?> clazz : widgetTypes) {
          if (clazz.isAssignableFrom(widget.getClass())) {
            typeMatch = true;
            break;
          }
        }
        if (typeMatch) {
          widgets.add(widget);
        }
      }
    }
    
    if (gridLayouts != null) {
      for (GridLayout grid : gridLayouts) {
        for (CellLayout cell : grid.getCells()) {
          Widget widget = cell.getWidget();
          boolean typeMatch = false;
          for (Class<?> clazz : widgetTypes) {
            if (clazz.isAssignableFrom(widget.getClass())) {
              typeMatch = true;
              break;
            }
          }
          if (typeMatch) {
            widgets.add(widget);
          }
        }
      }
    }
    return widgets;
  }

  @JsonManagedReference("screen-absolute")
  public List<AbsoluteLayout> getAbsoluteLayouts() {
    return absoluteLayouts;
  }
  
  @JsonManagedReference("screen-grid")
  public List<GridLayout> getGridLayouts() {
    return gridLayouts;
  }
  
  @JsonManagedReference("screen-gesture")
  public List<Gesture> getGestures() {
    return gestures;
  }
  
  public ScreenBackground getBackground() {
    return background;
  }
  
  public boolean isLandscape() {
    return landscape != null && landscape.booleanValue();
  }
  
  public Screen getInverseScreen() {
    if (inverseScreenId != null && parentGroup != null && inverseScreen == null) {
      for (Screen screen : parentGroup.getScreens()) {
        if (inverseScreenId.equals(screen.id)) {
          inverseScreen = screen;
          break;
        }
      }
    }
    
    return inverseScreen;
  }
}
