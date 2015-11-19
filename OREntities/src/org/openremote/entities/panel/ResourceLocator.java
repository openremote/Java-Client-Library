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

import org.openremote.entities.controller.AsyncControllerCallback;

/**
 * This interface defines the contract for retrieving resources from the controller; an instance is plugged into
 * the panel resource consumers when the panel is registered with the controller.
 * TODO : Add support for retrieving multiple resources at the same time on multiple threads
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 *
 */
public interface ResourceLocator {
  /**
   * Get the data of the named resource asynchronously (e.g. for an image it will get the byte[] of the actual image 
   * @param resourceName
   * @param resourceDataCallback
   */
  public void getResourceData(String resourceName, AsyncControllerCallback<ResourceDataResponse> resourceDataCallback);
  
  /**
   * Get the details of the named resource asynchronously
   * @param resourceName
   * @param resourceCallback
   */
  public void getResourceInfoDetails(String resourceName, AsyncControllerCallback<ResourceInfoDetails> resourceCallback);
  
  /**
   * Timeout in milliseconds used when retrieving resources through this resource locator   * 
   * @param timeout
   */
  public void setTimeout(int timeout);
}
