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

/**
 * Simple interface for defining a resource consumer (i.e. a panel component
 * that requires images)
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 *
 */
public interface ResourceConsumer extends NotifyPropertyChanged {
  public void setResourceLocator(ResourceLocator resourceLocator);
  
//  /**
//   * Gets a resource info for each resource that could be used by this
//   * resource consumer; optionally force load of the resource data rather
//   * than use lazy loading
//   * @return
//   */
//  public void getAllResourceInfos(boolean loadData, AsyncControllerCallback<ResourceInfo[]> callback);
  
//  /**
//   * Gets a resource info for a particular resource used by this
//   * resource consumer; optionally force load of the resource data rather
//   * than use lazy loading
//   * @param loadData
//   * @param callback
//   */
//  public void getResourceInfo(boolean loadData, AsyncControllerCallback<ResourceInfo> callback);
}
