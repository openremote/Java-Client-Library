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
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The background of screen, which contains background position in screen. The
 * position include absolute position and relative position.
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class ScreenBackground implements ResourceConsumer, NotifyPropertyChanged {
  public enum RelativePosition {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    CENTER,
    TOP_LEFT,
    BOTTOM_LEFT,
    TOP_RIGHT,
    BOTTOM_RIGHT
  };

  private Boolean fillScreen;
  @JsonProperty("image")
  ImageResource imageSrc;
  private Integer left;
  private Integer top;
  private RelativePosition relative;
  @JsonIgnore
  private ResourceInfo image;
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

  public Boolean getFillScreen() {
    return fillScreen;
  }

  String getImageName() {
    return imageSrc != null ? imageSrc.getSrc() : null;
  }

  public ResourceInfo getImage() {
    String imageName = getImageName();

    if (imageName != null && !imageName.isEmpty() && image == null) {
      image = new ResourceInfo(imageName, this);
    }
    return image;
  }

  public Integer getLeft() {
    return left;
  }

  public Integer getTop() {
    return top;
  }

  public RelativePosition getRelative() {
    return relative;
  }

  @Override
  public void onResourceChanged(String name) {
    if (name.equals(getImageName())) {
      raisePropertyChanged("image", image, image);
    }
  }

  @Override
  public List<ResourceInfo> getResources() {
    ResourceInfo img = getImage();
    return img == null ? null : Arrays.asList(new ResourceInfo[] { img });
  }
}
