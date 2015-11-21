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


/**
 * Interface for components that can send commands to the controller
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 *
 */
public interface CommandSender {
  /**
   * Try and send the specified control command asynchronously
   * @param command
   * @param callback
   */
  public void sendControlCommand(ControlCommand command, AsyncControllerCallback<ControlCommandResponse> callback);

  /**
   * Try and send the specified device command asynchronously
   * @param command
   * @param parameter
   * @param callback
   */
  public void sendCommand(Command command, String parameter, AsyncControllerCallback<CommandResponse> callback);
  
  /**
   * Sets the timeout in milliseconds for attempting to send a command
   * @param timeout
   */
  public void setTimeout(int timeout);
}
