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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Command {
  private int id;
  @JsonBackReference("device-command")
  private Device device;
  private String name;
  List<String> tags;
  private String protocol;

  public int getId() {
    return id;
  }

  public Device getDevice() {
    return device;
  }

  public String getName() {
    return name;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getProtocol() {
    return protocol;
  }
}
