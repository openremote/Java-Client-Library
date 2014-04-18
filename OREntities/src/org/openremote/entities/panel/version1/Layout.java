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
package org.openremote.entities.panel.version1;

/**
 * Abstract layout for all layouts that are positioned on a screen relative
 * to the top left
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
abstract class Layout {
  protected int height;
  protected int width;
  protected int left;
  protected int top;
  
  public int getHeight() {
    return height;
  }
  
  public int getWidth() {
    return width;
  }
  
  public int getLeft() {
    return left;
  }
  
  public int getTop() {
    return top;
  }
}
