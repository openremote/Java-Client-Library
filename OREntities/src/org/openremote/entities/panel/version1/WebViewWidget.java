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

import java.util.List;

import org.openremote.entities.panel.ResourceInfo;
import org.openremote.entities.panel.ResourceLocator;

/**
 * An iFrame for displaying a web page on a panel; src can be linked to a
 * sensor and can dynamically change
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class WebViewWidget extends SensoryWidget {
  String src;
  String username;
  String password;

  public String getSrc() {
    return src;
  }


  public String getUsername() {
    return username;
  }


  public String getPassword() {
    return password;
  }

  public Boolean getIsDynamic() {
    // For now can just check if sensor links > 0
    List<SensorLink> sensorLinks = getSensorLinks();
    return sensorLinks != null && sensorLinks.size() > 0;
  }
  
  private void setSrc(String src) {
    if (this.src.equalsIgnoreCase(src)) {
      return;
    }
    
    String oldValue = this.src;
    this.src = src;
    raisePropertyChanged("src", oldValue, src);
  }

  @Override
  public void onSensorValueChanged(int sensorId, String value) {
    StateMap matchedMap = getStateMap(sensorId, value); 
    String val = matchedMap != null ? matchedMap.getValue() : src;
    setSrc(val);
  }

  @Override
  public List<ResourceInfo> getResources() {
    return null;
  }


  @Override
  public void onResourceChanged(String name) {
  }
}
