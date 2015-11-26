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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Represents a device which has a name, list of commands it supports and list
 * of sensors it has available.
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

  /**
   * Get device name
   * 
   * @return {@link String}
   */
  public String getName() {
    return name;
  }

  /**
   * Get device commands
   * 
   * @return {@link List} of {@link Command}
   */
  public List<Command> getCommands() {
    return commands;
  }

  /**
   * Get device sensors
   * 
   * @return {@link List} of {@link Sensor}
   */
  public List<Sensor> getSensors() {
    return sensors;
  }

  /**
   * Find a device command by name (case insensitive)
   * 
   * @param name
   * @return {@link Command}
   */
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

  /**
   * Find device commands that contain one of the supplied tags
   * 
   * @param tags
   * @return {@link List} of {@link Command}
   */
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

  /**
   * Find sensor by name (case insensitive)
   * 
   * @param name
   * @return {@link Sensor} with specified name
   */
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

  /**
   * Wires up the device so it can send commands (this is done as part of
   * registration process and should not be used directly).
   * 
   * @param commandSender
   */
  public void setCommandSender(CommandSender commandSender) {
    this.commandSender = commandSender;
  }

  /**
   * Send a command to the device (the command must belong to this device)
   * 
   * @param command
   * @param callback
   */
  public void sendCommand(Command command, AsyncControllerCallback<CommandResponse> callback) {
    sendCommand(command, null, callback);
  }

  /**
   * Send a command and command parameter (e.g. slider value) to the device (the
   * command must belong to this device)
   * 
   * @param command
   * @param parameter
   * @param callback
   */
  public void sendCommand(Command command, String parameter,
          AsyncControllerCallback<CommandResponse> callback) {
    if (command != null && command.getDevice() == this && commandSender != null) {
      commandSender.sendCommand(command, parameter, callback);
    }
  }
}
