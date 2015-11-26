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
import org.openremote.entities.controller.ControllerResponseCode;

/**
 * This is a storage container for information about a named resource; depending
 * on resource loading options the data may or may not get loaded initially. The
 * name, modified time and content type are available when possible; modified
 * time will allow you to decide whether to use a cached copy of the image or to
 * get a newer one.
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 * 
 */
public class ResourceInfo {
  private ResourceInfoDetails details;
  private ResourceLocator locator;
  private String name;
  private byte[] data;
  private ResourceChangedCallback changedCallback;

  public ResourceInfo(String name, ResourceChangedCallback changedCallback) {
    this(null, name, changedCallback);
  }

  public ResourceInfo(ResourceLocator locator, String name, ResourceChangedCallback changedCallback) {
    this.locator = locator;
    this.name = name;
    this.changedCallback = changedCallback;
  }

  public String getName() {
    return name;
  }

  public void getDetails(final AsyncControllerCallback<ResourceInfoDetails> callback) {
    if (details != null) {
      callback.onSuccess(details);
      return;
    }

    if (locator == null) {
      callback.onFailure(ControllerResponseCode.RESOURCE_LOCATOR_NULL);
      return;
    }

    // Try and get the resource from the locator
    locator.getResourceInfoDetails(name, callback);
  }

  public boolean isDataLoaded() {
    return data != null;
  }

  public void getData(final AsyncControllerCallback<ResourceDataResponse> callback) {
    if (data != null) {
      callback.onSuccess(new ResourceDataResponse(name, data, ControllerResponseCode.OK));
      return;
    }

    if (locator == null) {
      callback.onFailure(ControllerResponseCode.RESOURCE_LOCATOR_NULL);
      return;
    }

    // Try and get the resource from the locator
    locator.getResourceData(name, callback);
  }

  /*
   * Mechanism for pushing resource locator resolver into each resource
   */
  public void setResourceLocator(ResourceLocator locator) {
    this.locator = locator;
    details = null;
    data = null;
    changedCallback.onResourceChanged(getName());
  }

  @Override
  public String toString() {
    return getName();
  }

}
