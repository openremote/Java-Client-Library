package org.openremote.entities.panel;

public class Color {
  private int red;
  private int green;
  private int blue;
  
  public int getRed() {
    return red;
  }
  public int getGreen() {
    return green;
  }
  public int getBlue() {
    return blue;
  }
  
  public Color(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }
  
  @Override
  public String toString() {
    red = Math.max(0, Math.min(255, red));
    green = Math.max(0, Math.min(255, green));
    blue = Math.max(0, Math.min(255, blue));

    // Convert RGB to HEX color code string
    return String.format("#%02X%02X%02X", red, green, blue);
  }
}
