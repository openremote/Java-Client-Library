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

import java.util.ArrayList;
import java.util.List;

import org.openremote.entities.panel.ButtonWidget;
import org.openremote.entities.panel.SwitchState;
import org.openremote.entities.panel.SwitchWidget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a device which has a name, list of commands it supports
 * and list of sensors it has available. 
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 *
 */
public class Device {
  private int id;
  private String name;
  @JsonManagedReference("device-command")
  private List<Command> commands;
  @JsonManagedReference("device-sensor")
  private List<Sensor> sensors;
  @JsonIgnore
  private CommandSender commandSender;
  
  public String getName() {
    return name;
  }
  public List<Command> getCommands() {
    return commands;
  }
  public List<Sensor> getSensors() {
    return sensors;
  }
  
  public Command findCommandByName(String name) {
    if (name == null || name.isEmpty() || commands == null) {
      return null;
    }
    for (Command command : commands) {
      if (command.getName().equalsIgnoreCase(name)) {
        return command;
      }
    }
    
    return null;
  }
  
  public List<Command> findCommandsByTags(List<String> tags) {
    if (tags == null || tags.size() == 0 || commands == null) {
      return null;
    }
    
    List<Command> matches = new ArrayList<Command>();
    for (Command command : commands) {
      if (command.getTags() != null) {
        boolean tagMatch = false;
        for (String searchTag : tags) {
          for (String tag : command.getTags()) {
            if (tag.equalsIgnoreCase(searchTag)) {
              matches.add(command);
              tagMatch = true;
              break;
            }
          }
          if (tagMatch) {
            break;
          }
        }
      }
    }
    
    return matches;
  }
  
  public Sensor findSensorByName(String name) {
    if (name == null || name.isEmpty() || sensors == null) {
      return null;
    }
    
    for (Sensor sensor : sensors) {
      if (sensor.getName().equalsIgnoreCase(name)) {
        return sensor;
      }
    }
    
    return null;
  }
  
  public void setCommandSender(CommandSender commandSender) {
    this.commandSender = commandSender;
  }

  public void sendCommand(Command command, AsyncControllerCallback<CommandResponse> callback) {
    sendCommand(command, null, callback);
  }
  
  public void sendCommand(Command command, String parameter, AsyncControllerCallback<CommandResponse> callback) {
    if (command != null && command.getDevice() == this && commandSender != null)
    {
      commandSender.sendCommand(command, parameter, callback);
    }
  }
}
