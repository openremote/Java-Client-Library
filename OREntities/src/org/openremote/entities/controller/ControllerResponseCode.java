/* OpenRemote, the Home of the Digital Home.
 * Copyright 2008-2012, OpenRemote Inc.
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
 * Controller Response Codes TODO: These need fully defining on the controller
 * REST API
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public enum ControllerResponseCode {
  OK(200, "OK"),
  BAD_COMMAND(400, "Invalid Command Request"),
  NOT_AUTHORIZED(401, "Please login"),
  FORBIDDEN(403, "Access Denied"),
  NOT_FOUND(404, "Controller Not Found"),
  COMMAND_ERROR(418, "Command Build Failed"),
  COMPONENT_INVALID(419, "No Such Component"),
  COMMAND_BUILDER_ERROR(420, "No Such Command Builder"),
  CONTROLLER_XML_NOT_FOUND(422, "Controller XML Not Found"),
  COMMAND_INVALID(423, "No Such Command"),
  CONTROLLER_XML_INVALID(424, "Controller or Panel XML is Invalid"),
  PANEL_XML_NOT_FOUND(426, "Panel XML Not Found"),
  PANEL_XML_INVALID(427, "Panel XML is Invalid"),
  PANEL_NOT_FOUND(428, "No such Panel"),
  XML_ERROR(429, "Invalid Controller XML Element"),
  NO_RESPONSE(430, "No Response from Controller"),
  TIME_OUT(504, "Timed out"),
  XML_CHANGED(506, "Controller XML has Changed"),
  INVALID_URL(996, "Controller URL is null or invalid"),
  XML_JSON_PARSING_ERROR(520, "XML to JSON Parsing Error"),
  DEVICE_NULL(992, "Supplied device is null"),
  PANEL_NULL(993, "Supplied panel is null"),
  ALREADY_REGISTERED(994, "Device or Panel already registered"),
  UNREGISTERED(995, "Device or Panel unregistered"),
  DISCONNECTED(996, "Controller was disconnected"),
  RESOURCE_NAME_INVALID(997, "Resource name is null or empty"),
  RESOURCE_LOCATOR_NULL(998, "Resource Locator is not defined"),
  UNKNOWN_ERROR(999, "Unkown Controller Error");

  private final int code;
  private final String description;

  ControllerResponseCode(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public static ControllerResponseCode getResponseCode(int code) {
    ControllerResponseCode result = ControllerResponseCode.UNKNOWN_ERROR;
    for (ControllerResponseCode message : ControllerResponseCode.values()) {
      if (message.getCode() == code) {
        result = message;
        break;
      }
    }
    return result;
  }
}
