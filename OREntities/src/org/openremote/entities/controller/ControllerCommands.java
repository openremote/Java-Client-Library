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
 * List of Supported Controller Commands
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 * 
 */
public enum ControllerCommands {
  GET_PANEL_LIST,
  GET_PANEL_LAYOUT,
  SEND_COMMAND,
  GET_SENSOR_STATUS,
  DO_SENSOR_POLLING,
  GET_ROUND_ROBIN_LIST,
  IS_ALIVE,
  IS_SECURE,
  LOGOUT
}
