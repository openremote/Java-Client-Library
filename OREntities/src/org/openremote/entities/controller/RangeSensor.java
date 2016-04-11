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

/**
 * An integer sensor with a customisable min/max value range
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 * 
 */
public class RangeSensor extends Sensor {
  private Integer value;

  public RangeSensor() {
    super(SensorType.RANGE);
  }

  private int min;
  private int max;

  public int getMin() {
    return min;
  }

  public int getMax() {
    return max;
  }

  public int getValue() {
    if (value == null) {
      value = 0;
      try {
        value = Integer.parseInt(super.getStringValue());
        value = Math.min(getMax(), value);
        value = Math.max(getMin(), value);
      } catch (NumberFormatException e) {
        // Do nothing as this really shouldn't happen anyway
      }
    }
    return value;
  }

  @Override
  protected void onValueChanged() {
    int oldValue = value == null ? 0 : value;
    value = null;
    raisePropertyChanged("value", oldValue, getValue());
  }
}
