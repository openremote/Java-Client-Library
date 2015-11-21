/*
 * OpenRemote, the Home of the Digital Home.
 * Copyright 2008-2015, OpenRemote Inc.
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

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.openremote.entities.controller.ControllerError;
import org.openremote.entities.controller.ControllerResponseCode;
import org.openremote.entities.controller.PanelInfoList;
import org.openremote.entities.util.JacksonProcessor;

/**
 * This test class provides unit testing of the OREntities ORM
 * Device 
 *  
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 *
 */
public class ORMTests {
  @Test
  public void ControllerErrorTest() {
    InputStream is = null;
    ControllerError error = null;
    
    try {
      // Test JSON
      is = this.getClass().getResourceAsStream("controllererror.json");
      error = JacksonProcessor.unMarshall(is, ControllerError.class);
    } catch (Exception e) {
      Assert.fail(e.getMessage());
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
    Assert.assertNotNull(error);
    Assert.assertNotNull(error.getResponse());
    Assert.assertEquals(ControllerResponseCode.TIME_OUT, error.getResponse());
  }  
  
  @Test
  public void PanelListTest() {
    InputStream is = null;
    PanelInfoList definition = null;
    
    try {
      // Get JSON document
      is = this.getClass().getResourceAsStream("panellist.json");
      definition = JacksonProcessor.unMarshall(is, PanelInfoList.class);
    } catch (Exception e) {
      Assert.fail(e.getMessage());
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
    Assert.assertNotNull(definition);
    
    Assert.assertNotNull(definition.getPanelInfos());
    Assert.assertEquals(3, definition.getPanelInfos().size());
    Assert.assertEquals("Razberry", definition.getPanelInfos().get(1).getName());
  }
  
  @Test
  public void DeviceListTest() {
    InputStream is = null;
    DeviceInfo[] devices = null;
    
    try {
      // Test JSON
      is = this.getClass().getResourceAsStream("devicelist.json");
      devices = JacksonProcessor.unMarshall(is, DeviceInfo[].class);
    } catch (Exception e) {
      Assert.fail(e.getMessage());
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
    Assert.assertNotNull(devices);
    Assert.assertNotNull(devices);
    Assert.assertEquals(5, devices.length);
    Assert.assertEquals("BUTTON TEST", devices[1].getName());
  }
}
