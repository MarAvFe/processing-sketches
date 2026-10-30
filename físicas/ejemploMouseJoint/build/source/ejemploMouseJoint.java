import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Iterator; 
import shiffman.box2d.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.collision.shapes.Shape; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.joints.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ejemploMouseJoint extends PApplet {









Box2DProcessing box2d;

ArrayList<Box> boxes;
MouseSpring spring;

public void setup() {
  
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  boxes = new ArrayList();
  spring = new MouseSpring();
}

public void draw() {
  background(0);
  box2d.step();
  boxes.add(new Box(width / 2, 200, 20, 20));
  spring.update();
  Iterator<Box> it = boxes.iterator();
  while (it.hasNext()) {
    Box b = it.next();
    if (b.y > width) {
      b.killBody();
      it.remove();
    }
  }
  for (Box b : boxes) {
    b.display();
  }
}

public void mousePressed() {
  for (Box b : boxes) {
    if (b.mouseOver()) {
      spring.bind(b);
    }
  }
}

public void mouseReleased() {
  spring.unbind();
}
class Box {
  Body body;
  float w, h, x, y;
  int c;
  Box(float x, float y, float w, float h) {
    this.w = w;
    this.h = h;
    this.x = x;
    this.y = y;
    makeBody(x, y);
    c = color(random(255), 0, random(255));
  }
  public void makeBody(float x, float y) {
    BodyDef bd = new BodyDef();
    bd.type = BodyType.DYNAMIC;
    bd.position.set(box2d.coordPixelsToWorld(x, y));
    body = box2d.createBody(bd);

    PolygonShape ps = new PolygonShape();
    ps.setAsBox(box2d.scalarPixelsToWorld(w/2), box2d.scalarPixelsToWorld(h/2));

    FixtureDef fd = new FixtureDef();
    fd.setShape(ps);
    fd.setDensity(1);
    fd.setFriction(0.5f);
    fd.setRestitution(0.5f);

    body.createFixture(fd);
    body.setLinearVelocity(new Vec2(random(-25, 25), random(0, 25)));
    body.setAngularVelocity(random(-5, 5));
  }
  public void killBody() {
    box2d.destroyBody(body);
  }
  public boolean mouseOver() {
    Vec2 worldPos = box2d.coordPixelsToWorld(mouseX, mouseY);
    Fixture f = body.getFixtureList();
    return f.testPoint(worldPos);
  }
  public void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    x = pos.x;
    y = pos.y;
    float a = body.getAngle();
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    fill(c);
    stroke(255);
    strokeWeight(3);
    rect(0, 0, w, h);
    popMatrix();
  }
}
class MouseSpring {
  MouseJoint mouseJoint;
  MouseSpring() {
    mouseJoint = null;
  }
  public void update() {
    if (mouseJoint != null) {
      Vec2 mouseWorld = box2d.coordPixelsToWorld(mouseX, mouseY);
      mouseJoint.setTarget(mouseWorld);
    }
  }
  public void bind(Box box) {
    MouseJointDef mjd = new MouseJointDef();
    mjd.bodyA = box2d.getGroundBody();
    mjd.bodyB = box.body;
    Vec2 mp = box2d.coordPixelsToWorld(mouseX, mouseY);
    mjd.target.set(mp);
    mjd.maxForce = 1000 * box.body.m_mass;
    mjd.frequencyHz = 5;
    mjd.dampingRatio = 1;
    
    mouseJoint = (MouseJoint)box2d.world.createJoint(mjd);
  }
  public void unbind() {
    if (mouseJoint != null) {
      box2d.world.destroyJoint(mouseJoint);
      mouseJoint = null;
    }
  }
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ejemploMouseJoint" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
