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

import org.openremote.entities.panel.WidgetContainer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Cell layout used inside a grid layout; cell can contain one widget
 * and is positioned within the grid by column, row, columnspan and rowspan
 * properties  
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class CellLayout implements WidgetContainer {
  @JsonBackReference("grid-cell")
  GridLayout parentGrid;
  @JsonManagedReference("cell-button")
  private ButtonWidget button;
  @JsonManagedReference("cell-image")
  private ImageWidget image;
  private LabelWidget label;
  private SliderWidget slider;
  @JsonProperty("switch")
  private SwitchWidget switchWidget;
  private WebViewWidget web;
  private int x;
  private int y;
  @JsonProperty("rowspan")
  private int rowSpan;
  @JsonProperty("colspan")
  private int colSpan;
  
  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }
  
  public int getRowSpan() {
    return rowSpan;
  }
  
  public int getColSpan() {
    return colSpan;
  }

  public Widget getWidget() {
    if (button != null) {
      return button;
    }
    if (image != null) {
      return image;
    }
    if (label != null) {
      return label;
    }
    if (slider != null) {
      return slider;
    }
    if (switchWidget != null) {
      return switchWidget;
    }
    if (web != null) {
      return web;
    }
    return null;
  }
}
