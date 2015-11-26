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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class for widgets that are capable of connecting to sensors.
 * Sensors are connected to various properties on the widgets and changes
 * to the sensor will result in property change events on the connected
 * property.
 * TODO: Sensor link should provide information about which property it is
 * linked to.
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 *
 */
public abstract class SensoryWidget extends Widget {
  @JsonProperty("link")
  private List<SensorLink> sensorLinks;
  
  public List<SensorLink> getSensorLinks() {
    if (sensorLinks == null) {
      sensorLinks = new ArrayList<SensorLink>();
    }
    
    return sensorLinks;
  }
  
  /**
   * Called by the controller service whenever a linked sensor's value
   * changes. The panel that owns this widget must be registered with the
   * controller service to receive notifications.
   * 
   * @param sensorId
   * @param value
   */
  public abstract void onSensorValueChanged(int sensorId, String value);
  
  protected StateMap getStateMap(int sensorId, String value) {
    StateMap matchedMap = null;
    
    for (SensorLink link : getSensorLinks()) {
      if (link.getRef() == sensorId && link.getStates() != null) {
        for (StateMap map : link.getStates()) {
          if (map.getName() != null && map.getName().equalsIgnoreCase(value)) {
            matchedMap = map;
            break;
          }
        }
        break;
      }
    }
    
    return matchedMap;
  }
}
