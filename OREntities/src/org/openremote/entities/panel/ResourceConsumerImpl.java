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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.openremote.entitites.controller.AsyncControllerCallback;
import org.openremote.entitites.controller.ControllerResponseCode;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Abstract implementation of a resource consumer providing property changed
 * notification for resource changed events
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 *
 */
public abstract class ResourceConsumerImpl implements ResourceConsumer {
  @JsonIgnore
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  @JsonIgnore
  private ResourceLocator resourceLocator;
  
  protected void raisePropertyChanged(String propertyName, Object oldValue, Object newValue) {
    pcs.firePropertyChange(propertyName, oldValue, newValue);
  }
  
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }

  protected ResourceLocator getResourceLocator() {
    return resourceLocator;
  }
  
  protected void resolveResource(final String resourceName, boolean loadData, AsyncControllerCallback<ResourceInfo> callback) {
    if (resourceLocator == null) {
      callback.onFailure(ControllerResponseCode.RESOURCE_LOCATOR_NULL);
      return;
    }
    
    if (resourceName == null || resourceName.isEmpty()) {
      callback.onFailure(ControllerResponseCode.RESOURCE_NAME_INVALID);
      return;
    }
    
    resourceLocator.getResource(resourceName, loadData, callback);
  }
  
//  protected void resolveResources(final String[] strings, boolean loadData, AsyncControllerCallback<ResourceInfo[]> callback) {
//    if (resourceLocator == null) {
//      callback.onFailure(ControllerResponseCode.RESOURCE_LOCATOR_NULL);
//      return;
//    }
//    
//    for (String resourceName : strings) {
//      if (resourceName == null || resourceName.isEmpty()) {
//        callback.onFailure(ControllerResponseCode.RESOURCE_NAME_INVALID);
//        return;
//      }
//    }
//    
//    resourceLocator.getResources(strings, loadData, callback);
//  }
  
  @Override
  public void setResourceLocator(ResourceLocator resourceLocator) {
    if (this.resourceLocator == resourceLocator) {
      return;
    }
    
    this.resourceLocator = resourceLocator;
    OnResourceLocatorChanged(resourceLocator);
  }

  protected abstract void OnResourceLocatorChanged(ResourceLocator resourceLocator);
  
//  public void getAllResourceInfos(boolean loadData, AsyncControllerCallback<ResourceInfo[]> callback) {
//    resolveResources(getAllResourceNames(), loadData, callback);    
//  }
  
//  protected abstract String[] getAllResourceNames();
}
