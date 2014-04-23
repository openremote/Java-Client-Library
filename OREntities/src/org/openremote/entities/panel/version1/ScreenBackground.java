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

import org.openremote.entities.controller.AsyncControllerCallback;
import org.openremote.entities.controller.ControllerResponseCode;
import org.openremote.entities.panel.ResourceConsumerImpl;
import org.openremote.entities.panel.ResourceInfo;
import org.openremote.entities.panel.ResourceLocator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The background of screen, which contains background position in screen.
 * The position include absolute position and relative position.
 *  
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class ScreenBackground extends ResourceConsumerImpl {
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
  
  public Boolean getFillScreen() {
    return fillScreen;
  }
  
  String getImageName() {
    return imageSrc != null ? imageSrc.getSrc() : null;
  }
  
  public ResourceInfo getImage() {
    return image;
  }
  
  private void setImage(ResourceInfo image) {
    if (this.image == image) {
      return;
    }
    
    ResourceInfo oldValue = this.image;
    this.image = image;
    raisePropertyChanged("image", oldValue, image);
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
  protected void OnResourceLocatorChanged(ResourceLocator resourceLocator) {
    String imageName = getImageName();
    
    if (imageName != null && !imageName.isEmpty()) {
      resolveResource(imageName, false, new AsyncControllerCallback<ResourceInfo>() {
        @Override
        public void onSuccess(ResourceInfo result) {
          setImage(result);
        }
        
        @Override
        public void onFailure(ControllerResponseCode error) {
          setImage(null);
        }
      });
    }
  }

//  @Override
//  protected String[] getAllResourceNames() {
//    return new String[] { getImageName() };
//  }
}
