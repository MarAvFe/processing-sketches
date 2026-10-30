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

public class box2D1 extends PApplet {






ArrayList<Box> boxes;
Box2DProcessing box2d;

public void setup(){
    
    background(0);
    boxes = new ArrayList();
    box2d = new Box2DProcessing(this);
    box2d.createWorld();
}

public void draw(){
    background(0);
    box2d.step();
    for(Box b : boxes){
        b.draw();
    }

    if(mousePressed){
        boxes.add(new Box(mouseX,mouseY,20,20));
    }
}
class Box{
  float x, y;
  float w, h;
  Body body;

  Box(float x, float y, float w, float h){
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;

      BodyDef bodyDef = new BodyDef();
      bodyDef.type = BodyType.DYNAMIC;
      bodyDef.position.set(box2d.coordPixelsToWorld(x,y));
      body = box2d.createBody(bodyDef);

      PolygonShape shape = new PolygonShape();
      float box2dW = box2d.scalarPixelsToWorld(w/2);
      float box2dH = box2d.scalarPixelsToWorld(h/2);
      shape.setAsBox(box2dW, box2dH);

      FixtureDef fixture = new FixtureDef();
      fixture.shape = shape;
      fixture.density = 1;
      fixture.friction = 0.3f;
      fixture.restitution = 10;

      body.createFixture(fixture);
  }

  public void draw(){
      fill(0, 180, 255);
      noStroke();
      rectMode(CENTER);
      //rect(x,y,w,h);

      Vec2 pos = box2d.getBodyPixelCoord(body);
      float a = body.getAngle();
      pushMatrix();
      translate(pos.x,pos.y);
      rotate(-a);
      rect(0,0,w,h);
      popMatrix();
  }

}
  public void settings() {  size(800,600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "box2D1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
