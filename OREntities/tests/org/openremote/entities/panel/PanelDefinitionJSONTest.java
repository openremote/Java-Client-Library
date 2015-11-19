package org.openremote.entities.panel;

import java.io.InputStream;
import java.util.List;

import org.junit.*;
import org.openremote.entities.panel.AbsoluteLayout;
import org.openremote.entities.panel.ButtonWidget;
import org.openremote.entities.panel.CellLayout;
import org.openremote.entities.panel.Gesture;
import org.openremote.entities.panel.GridLayout;
import org.openremote.entities.panel.Group;
import org.openremote.entities.panel.ImageWidget;
import org.openremote.entities.panel.LabelWidget;
import org.openremote.entities.panel.Navigation;
import org.openremote.entities.panel.Panel;
import org.openremote.entities.panel.Screen;
import org.openremote.entities.panel.ScreenBackground;
import org.openremote.entities.panel.SensorLink;
import org.openremote.entities.panel.SensoryWidget;
import org.openremote.entities.panel.SliderWidget;
import org.openremote.entities.panel.StateMap;
import org.openremote.entities.panel.SwitchWidget;
import org.openremote.entities.panel.TabBar;
import org.openremote.entities.panel.TabBarItem;
import org.openremote.entities.panel.WebViewWidget;
import org.openremote.entities.panel.Widget;
import org.openremote.entities.util.JacksonProcessor;

public class PanelDefinitionJSONTest {
  private static Panel definition;
  
  @BeforeClass
  public static void onStart() {
    try {
      // Get JSON document
      InputStream is = PanelDefinitionJSONTest.class.getResourceAsStream("panel.json");
      definition = JacksonProcessor.unMarshall(is, Panel.class);
      is.close();
    } catch (Exception e) {
      Assert.fail(e.getMessage());
    }
    
    
    Assert.assertNotNull(definition);
  }
    
  @Test
  public void panelDefinitionTest() {
    Assert.assertNotNull(definition.getTabBar());
    Assert.assertNotNull(definition.getGroups());
    Assert.assertNotNull(definition.getScreens());
  }

  @Test
  public void tabBarTest() {
    TabBar tabBar = definition.getTabBar();
    
    // Check item count matches xml
    List<TabBarItem> items = tabBar.getItems();
    Assert.assertNotNull(items);
    Assert.assertTrue(items.size() == 8);
    
    // Check item 2 & 5
    int[] ids = new int[] { 1, 4};
    for (int i = 0; i < ids.length; i++) {
      TabBarItem item = items.get(ids[i]);
      Assert.assertNotNull(item);
      Assert.assertNotNull(item);
      Assert.assertNotNull(item.imageSrc);
      Assert.assertEquals(i == 0 ? "Aircondition" : "Setting", item.getName());
      Assert.assertEquals(i == 0 ? "aircondition1274952362228.png" : "setting1274952617279.png", item.imageSrc.getSrc());
      Assert.assertNotNull(item.getNavigation());
      Assert.assertTrue((i == 0 && item.getNavigation().getToGroup() != null && item.getNavigation().getToScreen() != null) || (i == 1 && item.getNavigation().getTo() == Navigation.SystemScreenType.SETTINGS));
      i++;
    }
  }
  
  @Test
  public void groupsTest() {
    List<Group> groups = definition.getGroups();
    
    // Check item count matches xml
    Assert.assertNotNull(groups);
    Assert.assertTrue(groups.size() == 2);
    
    // Check both groups
    int i = 1;
    for (Group group : groups) {
      Assert.assertNotNull(group);
      Assert.assertNotNull(group.getScreens());
      Assert.assertEquals(i == 1 ? "Livingroom" : "Bedroom", group.getName());
      Assert.assertEquals(i == 1 ? 4 : 1, group.getScreens().size());

      // Check screen mapping
      int j=0;
      String[] names = i == 1 ? new String[] {"Light", "Light", "Aircondition", "TV"} : new String[] {"Navigate_Buttons"};
      for (Screen screen : group.getScreens()) {
        Assert.assertTrue(screen.getName().equals(names[j]));
        j++;
      }

      i++;
    }
  }
  
  @Test
  public void screensTest() {
    List<Screen> screens = definition.getScreens();
    
    // Check count
    Assert.assertNotNull(screens);
    Assert.assertTrue(screens.size() == 5);
    
    // Check screens
    String[] names = new String[] {"Light", "Light", "Aircondition", "TV", "Navigate_Buttons"};
    String[] backgrounds = new String[] {"lightbackground1274950957643.png", "500X3001274943015264.png", "air1274951021940.png", "tvbackground1274951037399.png", "yesanpoh1274939752102.jpg"};
    int[] absCounts = new int[] {5, 3, 5, 1, 1};

    for (int i = 0; i < screens.size(); i++) {
      Screen screen = screens.get(i);
      Assert.assertNotNull(screen);

      // Check name 
      Assert.assertEquals(names[i], screen.getName());
      
      // Check background
      ScreenBackground background = screen.getBackground();
      Assert.assertNotNull(background);
      Assert.assertEquals(i == 1 ? null : true, background.getFillScreen());
      Assert.assertEquals(i == 1 ? ScreenBackground.RelativePosition.TOP : null, background.getRelative());
      Assert.assertNotNull(background.imageSrc);
      Assert.assertEquals(backgrounds[i] , background.imageSrc.getSrc());
      
      // Check absolute sizes
      List<AbsoluteLayout> absolutes = screen.getAbsoluteLayouts();
      Assert.assertNotNull(absolutes);
      Assert.assertEquals(absCounts[i], absolutes.size());
      
      if (i == 0) {
        // Check absolute layouts 3, 4 and 5
        int[] indexes = new int[] {2, 3, 4};
        for (int j = 0; j < indexes.length; j++) {        
          AbsoluteLayout abs = absolutes.get(indexes[j]);

          Widget widget = abs.getWidget();
          Assert.assertNotNull(widget);
          
          switch(j)
          {
            case 0:
              // Only check width, height, left, top on one layout
              Assert.assertEquals(127, abs.getLeft());          
              Assert.assertEquals(174, abs.getTop());
              Assert.assertEquals(185, abs.getWidth());
              Assert.assertEquals(54, abs.getHeight());
              
              Assert.assertTrue(widget instanceof LabelWidget);
              LabelWidget label = (LabelWidget)widget;
              Assert.assertEquals(14, label.getFontSize());
              Assert.assertEquals("#0000FF", label.getColor());
              Assert.assertEquals("light status", label.getText());
              // Fall through and test sensor link
            case 1:
              if (j == 1) {
                Assert.assertTrue(widget instanceof SwitchWidget);
              }
              // Fall through and test sensor link
            case 2:
              if (j == 2) {
                Assert.assertTrue(widget instanceof ImageWidget);
                ImageWidget image = (ImageWidget)widget;
                Assert.assertNotNull(image.getLinkedLabel());
              }
              
              SensoryWidget sw = (SensoryWidget)widget;
              Assert.assertNotNull(sw.getSensorLinks());
              Assert.assertEquals(1, sw.getSensorLinks().size());
              SensorLink link = sw.getSensorLinks().get(0);
              Assert.assertNotNull(link);
              Assert.assertEquals(570, link.getRef());
              Assert.assertEquals("sensor", link.getType());
              Assert.assertEquals(2, link.getStates().size());
              for (int k = 0; k < link.getStates().size(); k++) {
                StateMap map = link.getStates().get(k);
                Assert.assertNotNull(map);
                Assert.assertEquals(k == 0 ? "off" : "on", map.getName());
                if (j == 0) {
                  Assert.assertEquals(k == 0 ? "light is off" : "light is on", map.getValue());
                } else if (j == 1) {
                  Assert.assertEquals(k == 0 ? "infrared.png" : "power.png", map.getValue());                  
                } else {
                  Assert.assertEquals(k == 0 ? "lightbulboff1274951408852.png" : "lightbulb1274951401848.png", map.getValue());
                }
              }
              break;
          }
        }
      }
      
      if (i == 1) {
        // Check gestures
        List<Gesture> gestures = screen.getGestures();
        Assert.assertNotNull(gestures);
        Assert.assertEquals(4, gestures.size());
        Gesture gesture = gestures.get(0);
        Assert.assertNotNull(gesture);
        Assert.assertTrue(gesture.getHasControlCommand());
        Assert.assertEquals(Gesture.GestureType.SWIPE_BOTTOM_TO_TOP, gesture.getType());
      }
      
      if (i == 2) {
        // Check absolute layout 3 - slider
        AbsoluteLayout abs = absolutes.get(2);
        Widget widget = abs.getWidget();
        Assert.assertNotNull(widget);
        Assert.assertTrue(widget instanceof SliderWidget);
        SliderWidget slider = (SliderWidget)widget;
        Assert.assertTrue(slider.isVertical());
        Assert.assertFalse(slider.isPassive());
        Assert.assertNotNull(slider.getThumbImageName());
        Assert.assertEquals("vthumb1274939161708.png", slider.getThumbImageName());
        Assert.assertEquals(0, slider.getMinValue());
        Assert.assertEquals(100, slider.getMaxValue());
        Assert.assertEquals("vmin1274939127956.png", slider.getMinImageName());
        Assert.assertEquals("vminTrack1274939151356.png", slider.getMinTrackImageName());
        Assert.assertEquals("vmax1274939183784.png", slider.getMaxImageName());
        Assert.assertEquals("vmaxTrack1274939173529.png", slider.getMaxTrackImageName());
      }
       
      if (i == 3) {
        // Check absolute layout 1 - webview
        AbsoluteLayout abs = absolutes.get(0);
        Widget widget = abs.getWidget();
        Assert.assertNotNull(widget);
        Assert.assertTrue(widget instanceof WebViewWidget);
        WebViewWidget web = (WebViewWidget)widget;
        Assert.assertEquals("http://test.com/", web.getSrc());
        Assert.assertEquals("user", web.getUsername());
        Assert.assertEquals("password", web.getPassword());
      }
      
      if (i == 4) {
        // Check grids
        List<GridLayout> grids = screen.getGridLayouts(); 
        Assert.assertNotNull(grids);
        Assert.assertEquals(1, grids.size());
        GridLayout grid = grids.get(0);
        Assert.assertEquals(4, grid.getColumns());
        Assert.assertEquals(3, grid.getRows());
        Assert.assertEquals(110, grid.getLeft());
        Assert.assertEquals(262, grid.getTop());
        Assert.assertEquals(200, grid.getWidth());
        Assert.assertEquals(150, grid.getHeight());
        
        // Check a cell
        List<CellLayout> cells = grid.getCells();
        Assert.assertNotNull(cells);        
        Assert.assertEquals(4, cells.size());
        CellLayout cell = cells.get(1);
        Assert.assertNotNull(cell);
        Assert.assertEquals(2, cell.getX());
        Assert.assertEquals(0, cell.getY());
        Assert.assertEquals(2, cell.getColSpan());
        Assert.assertEquals(1, cell.getRowSpan());
        Widget widget = cell.getWidget();
        Assert.assertNotNull(widget);
        Assert.assertTrue(widget instanceof ButtonWidget);
        Assert.assertEquals("Logout", widget.getName());
        ButtonWidget button = (ButtonWidget)widget;
        Navigation nav = button.getNavigation();
        Assert.assertNotNull(nav);
        Assert.assertEquals(Navigation.SystemScreenType.LOGOUT, nav.getTo());
      }
    }
  }
}
