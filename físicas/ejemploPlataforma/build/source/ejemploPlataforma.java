import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.common.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ejemploPlataforma extends PApplet {






ArrayList<Box> boxes;
Box2DProcessing box2d;

public void setup() {
  
  background(0);
  
  boxes = new ArrayList();
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
}

public void draw() {
  background(0);
  box2d.step();
  for (Box b : boxes) {
    b.draw();
  }
  if (mousePressed) {
    if (mouseButton == LEFT) {
      boxes.add(new Box(mouseX, mouseY, random(8, 64), random(8, 64)));
    } else {
      boxes.add(new Platform(mouseX, mouseY, random(80, 120), random(8, 32)));
    }
  }
}
class Box {
  float x, y;
  float w, h;
  int c;
  Body body;
  BodyType type;

  Box() {}
  Box(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.type = BodyType.DYNAMIC;
    c = color(255, 0, 0);
    createBody();
  }
  public void createBody() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = type;
    bodyDef.position.set(box2d.coordPixelsToWorld(x, y));
    bodyDef.setAngle(random(PI));
    bodyDef.setAngularVelocity(20);
    body = box2d.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    float box2dW = box2d.scalarPixelsToWorld(w/2);
    float box2dH = box2d.scalarPixelsToWorld(h/2);
    shape.setAsBox(box2dW, box2dH);

    FixtureDef fixture = new FixtureDef();
    fixture.shape = shape;
    fixture.density = 1;
    fixture.friction = 10;
    fixture.restitution = 0.4f;

    body.createFixture(fixture);
  }
  public void draw() {
    fill(0, 180, 255);
    stroke(255);
    fill(c);
    rectMode(CENTER);

    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    rect(0, 0, w, h);
    popMatrix();
  }
}
class Platform extends Box {
  Platform(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    type = BodyType.STATIC;
    c = color(0, 255, 0);
    createBody();
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ejemploPlataforma" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
