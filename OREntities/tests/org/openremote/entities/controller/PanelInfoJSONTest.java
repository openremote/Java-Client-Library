package org.openremote.entities.controller;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openremote.entities.controller.PanelInfoList;
import org.openremote.entities.panel.version1.PanelDefinitionJSONTest;
import org.openremote.entities.util.JacksonProcessor;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PanelInfoJSONTest {
  private static PanelInfoList definition;
  
  @BeforeClass
  public static void onStart() {
    try {
      // Get JSON document
      InputStream is = PanelInfoJSONTest.class.getResourceAsStream("panellist.json");
      definition = JacksonProcessor.unMarshall(is, PanelInfoList.class);
      is.close();
    } catch (Exception e) {
      Assert.fail(e.getMessage());
    }
    
    Assert.assertNotNull(definition);
  }
  
  @Test
  public void PanelListTest() {
    Assert.assertNotNull(definition.getPanelInfos());
    Assert.assertEquals(3, definition.getPanelInfos().size());
    Assert.assertEquals("Razberry", definition.getPanelInfos().get(1).getName());
  }
}
