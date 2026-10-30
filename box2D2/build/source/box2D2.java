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

public class box2D2 extends PApplet {






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
        if(mouseButton == LEFT){
            boxes.add(new Box(mouseX,mouseY,20,20));
        }else{
            boxes.add(new Platform(mouseX,mouseY,20,20));
        }

    }
}
class Box{
  float x, y;
  float w, h;
  Body body;
  BodyType bodyType;

  Box(){}
  Box(float x, float y, float w, float h){
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.bodyType = BodyType.DYNAMIC;

      color(0,32,32);
      createBody();
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

  public void createBody(){
      BodyDef bodyDef = new BodyDef();
      bodyDef.type = bodyType;
      bodyDef.position.set(box2d.coordPixelsToWorld(x,y));
      bodyDef.setAngle(random(PI));
      body = box2d.createBody(bodyDef);

      PolygonShape shape = new PolygonShape();
      float box2dW = box2d.scalarPixelsToWorld(w/2);
      float box2dH = box2d.scalarPixelsToWorld(h/2);
      shape.setAsBox(box2dW, box2dH);

      FixtureDef fixture = new FixtureDef();
      fixture.shape = shape;
      fixture.density = 1;
      fixture.friction = 0.3f;
      fixture.restitution = 0.1f;

      body.createFixture(fixture);
  }

}
class Platform extends Box{

  Platform(float x, float y, float w, float h){
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.bodyType = BodyType.STATIC;

      color(128,128,0);
      createBody();
  }
}
  public void settings() {  size(800,600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "box2D2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
