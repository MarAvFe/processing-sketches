import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class movimientoAngular extends PApplet {

float ang = PI/4;
float velAng = 0;
float accAng = 0;

public void setup() {
  
}

public void draw() {
  ang += velAng;
  velAng += accAng;
  accAng = map(mouseX, 0, width, -0.001f, 0.001f);
  background(0);
  rectMode(CENTER);
  noStroke();
  fill(128);

  pushMatrix();
  translate(width/2, height/2);
  rotate(ang);
  rect(0, 0, 350, 200);
  popMatrix();

  pushMatrix();
  translate(200, 200);
  rotate(ang);
  rect(0, 0, 100, 10);
  popMatrix();
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "movimientoAngular" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
