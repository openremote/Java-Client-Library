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

import org.openremote.entities.panel.Color;

public class ColorSensor extends Sensor {
  private Color color;

  public ColorSensor() {
    super(SensorType.COLOR);
  }

  public Color getValue() {
    if (color == null) {
      String str = super.getStringValue();
      if (str != null) {
        str = str.replace("#", "");

        if (str.length() == 6) {
          try {
            color = new Color(Integer.valueOf(str.substring(1, 3), 16), Integer.valueOf(
                    str.substring(3, 5), 16), Integer.valueOf(str.substring(5, 7), 16));
          } catch (NumberFormatException e) {
          }
        }
      }
    }
    return color;
  }

  @Override
  protected void onValueChanged() {
    Color oldValue = color;
    color = null;
    raisePropertyChanged("value", oldValue, getValue());
  }
}
