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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used for marshalling to/from JSON
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
class LabelInclude {
  @JsonIgnore
  LabelWidget label;
  @JsonProperty("ref")
  int labelRef;
  private String type = "label";

  LabelWidget getLabel() {
    return label;
  }

  String getType() {
    return type;
  }

  LabelInclude() {
  }

  LabelInclude(LabelWidget label) {
    this.label = label;
  }
}
