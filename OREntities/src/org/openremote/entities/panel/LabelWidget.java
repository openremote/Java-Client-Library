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

import java.util.List;

/**
 * The label can set font size and color, and text can be linked to a sensor
 * (i.e. dynamic).
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class LabelWidget extends SensoryWidget {
  private String text;
  private String color;
  private int fontSize;
  private boolean isInitialised;

  public String getText() {
    return text;
  }

  public String getColor() {
    return color;
  }

  public int getFontSize() {
    return fontSize;
  }

  public Boolean getIsDynamic() {
    // For now can just check if sensor links > 0
    List<SensorLink> sensorLinks = getSensorLinks();
    return sensorLinks != null && sensorLinks.size() > 0;
  }

  @Override
  public void onSensorValueChanged(int sensorId, String value) {
    // Only sensor linking supported at present is for text property
    StateMap matchedMap = getStateMap(sensorId, value);
    String val = matchedMap != null ? matchedMap.getValue() : value;
    setText(val);
  }

  @Override
  public String getName() {
    return isInitialised ? name : text;
  }

  synchronized private void setText(String value) {
    if (!isInitialised) {
      name = text;
      isInitialised = true;
    }

    if (text.equals(value)) {
      return;
    }

    String oldValue = text;
    text = value;
    raisePropertyChanged("text", oldValue, text);
  }

  @Override
  public List<ResourceInfo> getResources() {
    return null;
  }

  @Override
  public void onResourceChanged(String name) {
  }
}
