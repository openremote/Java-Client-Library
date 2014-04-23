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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Tab Bar Item can be used for navigation to other screens
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class TabBarItem extends ResourceConsumerImpl {
  @JsonBackReference("tabbar-tabbaritem")
  TabBar parentTabBar;
  
  private String name;
  @JsonProperty("navigate")
  @JsonManagedReference("tabbaritem-navigation")
  private Navigation navigation;
  @JsonProperty("image")
  ImageResource imageSrc;
  @JsonIgnore
  private ResourceInfo image;

  public String getName() {
    return name;
  }
  
  void setName(String name) {
    this.name = name;
  }

  public Navigation getNavigation() {
    return navigation;
  }
  
  void setNavigation(Navigation navigation) {
    this.navigation = navigation;
  }
  
  private String getImageName() {
    return image != null ? imageSrc.getSrc() : null;
  }
  
  public ResourceInfo getImage() {
    return image;
  }
  
  void setImage(ResourceInfo image) {
    if (this.image == image) {
      return;
    }
    
    ResourceInfo oldValue = this.image;
    this.image = image;
    
    raisePropertyChanged("image", oldValue, image);
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
//    return new String[] {getImageName()};
//  }
}
