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

import java.util.HashMap;
import java.util.Map;

import org.openremote.entities.panel.ResourceInfo;
import org.openremote.entities.panel.ResourceLocator;
import org.openremote.entitites.controller.AsyncControllerCallback;
import org.openremote.entitites.controller.ControllerResponseCode;

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
  private Map<String, ResourceInfo> images = new HashMap<String, ResourceInfo>();
  private String src;
  @JsonIgnore
  private String currentImageSrc;
  @JsonIgnore
  private ResourceInfo currentImage;
  @JsonIgnore
  LabelWidget linkedLabel;
  @JsonProperty("include")
  LabelInclude linkedLabelInclude;
  @JsonIgnore
  boolean linkedLabelChecked;
  @JsonIgnore
  private ResourceLocator resourceLocator;
  
  public ResourceInfo getCurrentImage() {
    return currentImage;
  }
  
  private String getCurrentImageSrc() {
    return currentImageSrc != null ? currentImageSrc : src;  
  }
  
  private void setCurrentImageSrc(String imageSrc) {
    if (getCurrentImageSrc().equals(imageSrc)) {
      return;
    }
    
    currentImageSrc = imageSrc;
    
    resolveResource(imageSrc);
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
  
  private void setCurrentImage(ResourceInfo image) {
    if (this.currentImage == image) {
      return;
    }
    
    ResourceInfo oldValue = this.currentImage;
    this.currentImage = image;
    raisePropertyChanged("currentImage", oldValue, image);
  }
  
  private void resolveResource(final String resourceName) {
    if (images.containsKey(resourceName)) {
      setCurrentImage(images.get(resourceName));
      return;
    }
    
    resolveResource(resourceName, false, new AsyncControllerCallback<ResourceInfo>() {
      @Override
      public void onSuccess(ResourceInfo result) {
        if (getCurrentImageSrc().equals(resourceName)) {
          setCurrentImage(result);
        }
      }
      
      @Override
      public void onFailure(ControllerResponseCode error) {
        if (getCurrentImageSrc().equalsIgnoreCase(resourceName)) {
          setCurrentImage(null);
        }
      }
    });
  }

  @Override
  public void onSensorValueChanged(int sensorId, String value) {
    // Change image to match sensor value if sensor link set
    StateMap matchedMap = getStateMap(sensorId, value); 
    setCurrentImageSrc(matchedMap != null ? matchedMap.getValue() : src);
  }

  @Override
  protected void OnResourceLocatorChanged(ResourceLocator resourceLocator) {
    // Try and resolve the current image
    resolveResource(getCurrentImageSrc());    
  }

//  @Override
//  protected String[] getAllResourceNames() {
//    List<String> names = new ArrayList<String>();
//    names.add(src);
//    
//    // Iterate through the state map and extract images
//    List<SensorLink> links = getSensorLinks();
//    SensorLink link = null;
//    
//    if (links != null && links.size() > 0) {
//      link = links.get(0);
//    }
//    
//    if (link != null) {
//      for (StateMap map : link.getStates()) {
//        names.add(map.getValue());
//      }
//    }
//    
//    return names.toArray(new String[0]);
//  }
}
