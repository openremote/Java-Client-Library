package org.openremote.entities.controller;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openremote.entities.controller.ControllerError;
import org.openremote.entities.controller.ControllerResponseCode;
import org.openremote.entities.controller.PanelInfoList;
import org.openremote.entities.panel.PanelDefinitionJSONTest;
import org.openremote.entities.util.JacksonProcessor;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ControllerErrorJSONTest {
  @Test
  public void PanelListTest() {
    try {
      // Get JSON document
      InputStream is = ControllerErrorJSONTest.class.getResourceAsStream("controllererror.json");
    
      ControllerError response = JacksonProcessor.unMarshall(is, ControllerError.class);
      is.close();
      Assert.assertNotNull(response);
      Assert.assertNotNull(response.getResponse());
      Assert.assertEquals(ControllerResponseCode.TIME_OUT, response.getResponse());
    } catch (Exception e) {
      Assert.fail(e.getMessage());
    }
  }
}
