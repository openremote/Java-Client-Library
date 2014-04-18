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
 * Absolute layout that can contain one widget and is positioned relative
 * to the top left of the containing screen.
 * It parse the absolute node, contains size and position info.
 *  
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class AbsoluteLayout extends Layout implements WidgetContainer {
  @JsonBackReference("screen-absolute")
  Screen parentScreen;
  @JsonManagedReference("absolute-button")
  private ButtonWidget button;
  @JsonManagedReference("absolute-image")
  private ImageWidget image;
  private LabelWidget label;
  private SliderWidget slider;
  @JsonProperty("switch")
  private SwitchWidget switchWidget;
  private WebViewWidget web;

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
