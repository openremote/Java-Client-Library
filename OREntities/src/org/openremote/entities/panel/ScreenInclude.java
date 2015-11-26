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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used for marshalling to/from JSON
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
class ScreenInclude {
  @JsonBackReference("group-screeninclude")
  Group parentGroup;
  private Screen screen;
  @JsonProperty("ref")
  int screenRef;
  private String type = "screen";

  Screen getScreen() {
    if (screen == null) {
      List<Screen> screens = parentGroup.parentGroupList.parentPanel.getScreens();
      for (Screen screen : screens) {
        if (screen.id == screenRef) {
          this.screen = screen;
          break;
        }
      }
    }
    return screen;
  }

  String getType() {
    return type;
  }
}
