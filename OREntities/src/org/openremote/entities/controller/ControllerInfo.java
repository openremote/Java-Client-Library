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
package org.openremote.entities.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains information about a controller; this information can be used
 * to recreate an instance of the controller 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public final class ControllerInfo {
  @JsonProperty("controllerName")
  private String name;
  @JsonProperty("version")
  private String version;
  @JsonProperty("controllerURL")
  private String url;
  @JsonProperty("controllerIdentity")
  private String identity;

  void setName(String name) {
    this.name = name;
  }

  void setVersion(String version) {
    this.version = version;
  }

  void setUrl(String url) {
    this.url = url;
  }

  void setIdentity(String identity) {
    this.identity = identity;
  }
  
  ControllerInfo() {
  }

  public ControllerInfo(String url) {
    this.url = url;
  }
  
  public ControllerInfo(String url, String name, String version, String identity) {
    this.name = name;
    this.version = version;
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public String getUrl() {
    return url;
  }
  
  public String getIdentity() {
    return identity;
  }
}
