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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base class for all widgets. All widget properties are read-only (although not
 * truly immutable in all cases). All widgets provide property change
 * notification and resource consumer functionality but may not use either.
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 * 
 */
public abstract class Widget implements ResourceConsumer, NotifyPropertyChanged {
  protected int id;
  protected String name;
  @JsonIgnore
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  protected void raisePropertyChanged(String propertyName, Object oldValue, Object newValue) {
    pcs.firePropertyChange(propertyName, oldValue, newValue);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }

  public String getName() {
    return name;
  }
}
