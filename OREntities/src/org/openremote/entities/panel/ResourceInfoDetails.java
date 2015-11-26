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
package org.openremote.entities.panel;

import java.util.Date;

/**
 * A simple wrapper for all details relating to a resource (excluding the actual
 * binary data of the resource)
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 * 
 */
public class ResourceInfoDetails {
  private Date modifiedTime;
  private String contentType;

  public ResourceInfoDetails(Date modifiedTime, String contentType) {
    this.modifiedTime = modifiedTime;
    this.contentType = contentType;
  }

  public Date getModifiedTime() {
    return modifiedTime;
  }

  public String getContentType() {
    return contentType;
  }
}
