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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openremote.entities.controller.AsyncControllerCallback;
import org.openremote.entities.controller.ControllerResponseCode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An image widget capable of dynamically changing the image based
 * on a sensor value.
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 */
public class ImageWidget extends SensoryWidget {
  @JsonBackReference("absolute-image")
  AbsoluteLayout parentAbsolute;
  @JsonBackReference("cell-image")
  CellLayout parentCell;
  @JsonIgnore
  private Map<String, ResourceInfo> images;
  private String src;
  @JsonIgnore
  private String currentImageName;
  @JsonIgnore
  private ResourceInfo currentImage;
  @JsonIgnore
  private ResourceInfo defaultImage;
  @JsonIgnore
  LabelWidget linkedLabel;
  @JsonProperty("include")
  LabelInclude linkedLabelInclude;
  @JsonIgnore
  boolean linkedLabelChecked;
  @JsonIgnore
  private ResourceLocator resourceLocator;
  private static final String DEFAULT_KEY = "@*%#DEFAULT@*%#";
  
  private Map<String, ResourceInfo> getImageMap() {
    if (images == null) {
      images = new HashMap<String, ResourceInfo>();
      if (src != null && !src.isEmpty()) {
        images.put(DEFAULT_KEY, new ResourceInfo(src, this));
      }
      
      for (SensorLink link : getSensorLinks()) {
        if (link.getStates() != null) {
          for (StateMap map : link.getStates()) {
            images.put(map.getName(), new ResourceInfo(map.getValue(), this));
          }
        }
      }
    }
       
    return images;
  }
  
  public ResourceInfo getCurrentImage() {
    if (currentImage == null) {
      if (getCurrentImageName() != null && !getCurrentImageName().isEmpty()) {
        currentImage = getImageFromMap(getCurrentImageName());
      }
      
      if (currentImage == null) {
        currentImage = getImageFromMap(src);
      }
    }    
    
    return currentImage;
  }
  
  private ResourceInfo getImageFromMap(String imageName) {
    for(ResourceInfo imageResource : getImageMap().values()) {
      if (imageResource.getName().equalsIgnoreCase(imageName)) {
        return imageResource;
      }
    }
    
    return null;
  }
  
  private String getCurrentImageName() {
    return currentImageName;  
  }
  
  private void setCurrentImageName(String imageName) {
    if (getCurrentImageName() != null && getCurrentImageName().equals(imageName)) {
      return;
    }
    
    currentImageName = imageName;
    ResourceInfo oldImage = currentImage;
    currentImage = null;
    raisePropertyChanged("currentImage", oldImage, getCurrentImage());
  }
  
  public LabelWidget getLinkedLabel() {
    if (linkedLabelInclude != null && linkedLabel == null && !linkedLabelChecked) {
      linkedLabelChecked = true;
      
      // Find this linked label by looking through all the panel widgets
      Panel panel = null;
      if (parentAbsolute != null) {
        panel = parentAbsolute.parentScreen.parentScreenList.parentPanel;
      } else if (parentCell != null) {
        panel = parentCell.parentGrid.parentScreen.parentScreenList.parentPanel;
      }
      
      if (panel != null) {
        for (Widget widget : panel.getWidgets(new Class[] {LabelWidget.class})) {
          if (widget.id == linkedLabelInclude.labelRef) {
            linkedLabel = (LabelWidget)widget;
            break;
          }
        }
      }      
    }
    return linkedLabel;
  }
    
  @Override
  public void onSensorValueChanged(int sensorId, String value) {
    StateMap matchedMap = getStateMap(sensorId, value); 
    setCurrentImageName(matchedMap != null ? matchedMap.getValue() : null);
  }

  @Override
  public List<ResourceInfo> getResources() {
    return new ArrayList<ResourceInfo>(getImageMap().values());
  }

  @Override
  public void onResourceChanged(String name) {
    if (currentImage != null && currentImage.getName().equals(name)) {
      raisePropertyChanged("currentImage", currentImage, currentImage);
    }
  }
}
