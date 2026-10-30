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

public class aceleracionMouse2 extends PApplet {

Ball b;

public void setup() {
  
  b = new Ball(width/2, height/2, 0, 0);
}

public void draw() {
  background(0);
  PVector gravity = new PVector(0, 0.5f);
  b.aplicarFuerza(gravity);

  if (mousePressed) {
    PVector viento = new PVector(mouseX, mouseY);
    viento.sub(b.pos);
    viento.setMag(-1);
    b.aplicarFuerza(viento);
  }

  b.draw();
  b.refresh();
}
class Ball {
  PVector pos;
  PVector speed;
  PVector acc;

  Ball(float x, float y, float speedX, float speedY) {
    pos = new PVector (x, y);
    speed = new PVector (speedX, speedY);
    acc = new PVector(0, 0);
  }

  public void refresh() {
    speed.add(acc);
    speed.limit(5);
    pos.add(speed);
    acc.mult(0);
    pos.x  = pos.x % width;
    pos.y = pos.y % height;
  }

  public void draw() {
    noStroke();
    fill(255);
    ellipse(pos.x, pos.y, 40, 40);
  }

  public void aplicarFuerza(PVector fuerza) {
    acc.add(fuerza);
  }
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "aceleracionMouse2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
