/*
 * OpenRemote, the Home of the Digital Home.
 * Copyright 2008-2015, OpenRemote Inc.
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
package org.openremote.entities.controller;

import java.util.List;

/**
 * A text based sensor that provides value mapping via states
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 * 
 */
public class CustomSensor extends Sensor {
  private String value;
  List<String> states;

  public CustomSensor() {
    super(SensorType.CUSTOM);
  }

  public List<String> getStates() {
    return states;
  }

  public String getValue() {
    if (value == null) {
      value = super.getStringValue();
    }

    return value;
  }

  @Override
  protected void onValueChanged() {
    String oldValue = value;
    value = null;
    raisePropertyChanged("value", oldValue, getValue());
  }
}
