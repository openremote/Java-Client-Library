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

import org.openremote.entitites.controller.ControllerResponseCode;

/**
 * Response object encapsulating resource data for a particular resource
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 *
 */
public class ResourceDataResponse {
  private byte[] data;
  private String resourceName;
  ControllerResponseCode responseCode;
  
  public ResourceDataResponse(String resourceName, byte[] data, ControllerResponseCode responseCode) {
    this.resourceName = resourceName;
    this.data = data;
    this.responseCode = responseCode; 
  }

  public String getResourceName() {
    return resourceName;
  }
  
  public byte[] getData() {
    return data;
  }

  public ControllerResponseCode getResponseCode() {
    return responseCode;
  }
}
