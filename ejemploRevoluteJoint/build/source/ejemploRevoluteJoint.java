import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.contacts.*; 
import org.jbox2d.dynamics.joints.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ejemploRevoluteJoint extends PApplet {








Box2DProcessing box2d;

Surface surface;
ArrayList<Particle> particles;
ArrayList<Spring> springs;
Propeller propeller;

public void setup() {
  //size(800, 600);
  
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  ArrayList<Vec2> points = new ArrayList();
  points.add(new Vec2(width / 2 - 250, height * 0.8f));
  points.add(new Vec2(width / 2 + 250, height * 0.8f));
  points.add(new Vec2(width / 2 + 250, height * 0.85f));
  points.add(new Vec2(width / 2 - 250, height * 0.85f));
  surface = new Surface();
  propeller = new Propeller(width / 2, height * 0.8f);
  particles = new ArrayList();
  springs = new ArrayList();
  surface = new Surface();
}

public void draw() {
  background(0);
  box2d.step();
  surface.display();
  propeller.display();
  for (Particle p : particles) {
    p.display();
  }
  for (Spring s : springs) {
    s.display();
  }
}

public void mousePressed() {
  Particle p1 = new Particle(mouseX + random(-5, 5), mouseY + random(-5, 5), random(5, 10));
  Particle p2 = new Particle(mouseX + random(-5, 5), mouseY + random(-5, 5), random(5, 10));
  Spring s = new Spring(p1, p2, random(40, 70), random(0.1f, 10), random(1));
  particles.add(p1);
  particles.add(p2);
  springs.add(s);
}
class Box {
  Body body;
  float w, h;
  int c;
  
  Box(float x, float y, float w, float h) {
    this.w = w;
    this.h = h;
    makeBody(x, y);
    c = color(random(255), 0, random(255));
  }
  public void makeBody(float x, float y) {
    BodyDef bd = new BodyDef();
    bd.position.set(box2d.coordPixelsToWorld(x, y));
    bd.type = BodyType.STATIC;
    body = box2d.world.createBody(bd);

    PolygonShape ps = new PolygonShape();
    ps.setAsBox(box2d.scalarPixelsToWorld(w / 2), box2d.scalarPixelsToWorld(h / 2));    
    
    FixtureDef fd = new FixtureDef();
    fd.setShape(ps);
    fd.setDensity(1);
    fd.setFriction(0.01f);
    fd.setRestitution(0.5f);
    
    body.createFixture(fd);
  }
  public void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    fill(c);
    stroke(255);
    strokeWeight(3);
    rectMode(CENTER);
    rect(0, 0, w, h);
    popMatrix();
  }
}
class OldBike {
    PolygonParticle bigWheel;
    PolygonParticle smallWheel;
    Box base;
    Joint joint;
    float w = 10;
    float h = 180;

    OldBike(float x, float y) {
        base = new Box(x, y - h/2, w, h);
        spike = new PolygonParticle(x, y - h + w/2);

        RevoluteJointDef rjd = new RevoluteJointDef();
        rjd.initialize(base.body, spike.body, spike.body.getTransform().p);
        rjd.motorSpeed = PI * 1000;
        rjd.enableMotor = true;

        joint = box2d.world.createJoint(rjd);
    }
    public void display() {
        base.display();
        spike.display();
    }
}
class Particle {
  Body body;
  float r;
  
  Particle() {}
  Particle(float x, float y, float r) {
    this.r = r;
    makeBody(x, y);
  }
  public void makeBody(float x, float y) {
    BodyDef bd = new BodyDef();
    bd.position = box2d.coordPixelsToWorld(x, y);
    bd.type = BodyType.DYNAMIC;
    body = box2d.world.createBody(bd);
    
    CircleShape cs = new CircleShape();
    cs.setRadius(box2d.scalarPixelsToWorld(r));
    
    FixtureDef fd = new FixtureDef();
    fd.setShape(cs);
    fd.setDensity(1);
    fd.setFriction(0.1f);
    fd.setRestitution(0.3f);
    
    body.createFixture(fd);
    
    body.setLinearVelocity(new Vec2(random(-10, 10), random(0, 10)));
    body.setAngularVelocity(random(-10, 10));
  }
  public void killBody() {
    box2d.destroyBody(body);
  }
  public boolean done() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    if (pos.y > height) {
      killBody();
      return true;
    }
    return false;
  }
  public void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    fill(0xffFF0062);
    stroke(255);
    strokeWeight(2);
    ellipse(0, 0, r * 2, r * 2);
    line(-r, 0, r, 0);
    line(0, -r, 0, r);
    popMatrix();
  }
}
class PolygonParticle {
  Body body;
  ArrayList<Vec2> vertices;
  int c;

  PolygonParticle() {
  }
  PolygonParticle(float x, float y) {
    vertices = new ArrayList();

    vertices.add(new Vec2(0, -120));
    vertices.add(new Vec2(10, 5));
    vertices.add(new Vec2(-10, 5));

    makeBody(x, y);
    c = color(random(255), 0, random(255));
  }
  public void makeBody(float x, float y) {
    PolygonShape ps = new PolygonShape();
    Vec2[] worldVertices = new Vec2[vertices.size()];
    for (int i = 0; i < vertices.size(); i++) {
      worldVertices[i] = box2d.vectorPixelsToWorld(vertices.get(i));
    }
    ps.set(worldVertices, worldVertices.length);

    FixtureDef fd = new FixtureDef();
    fd.setShape(ps);
    fd.setDensity(1);
    fd.setRestitution(0.7f);
    fd.setFriction(0);
    BodyDef bd = new BodyDef();
    bd.setPosition(box2d.coordPixelsToWorld(x, y));
    bd.setType(BodyType.DYNAMIC);
    body = box2d.createBody(bd);
    body.createFixture(fd);
  }

  public void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    PolygonShape ps = (PolygonShape) body.getFixtureList().getShape();
    float a = body.getAngle();
    rectMode(CENTER);
    stroke(255);
    strokeWeight(3);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    fill(c);
    beginShape();
    for (int i = 0; i < ps.getVertexCount(); i++) {
      Vec2 v = box2d.vectorWorldToPixels(ps.getVertex(i));
      vertex(v.x, v.y);
    }
    endShape(CLOSE);
    popMatrix();
  }
  public boolean done() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    if (pos.y > height) {
      killBody();
      return true;
    }
    return false;
  }
  public void killBody() {
    box2d.destroyBody(body);
  }
}
class Propeller {
  PolygonParticle spike;
  Box base;
  Joint joint;
  float w = 10;
  float h = 180;

  Propeller(float x, float y) {
    base = new Box(x, y - h/2, w, h);
    spike = new PolygonParticle(x, y - h + w/2);

    RevoluteJointDef rjd = new RevoluteJointDef();
    rjd.initialize(base.body, spike.body, spike.body.getTransform().p);
    rjd.motorSpeed = PI * 1000;
    rjd.enableMotor = true;

    joint = box2d.world.createJoint(rjd);
  }
  public void display() {
    base.display();
    spike.display();
  }
}
class Spring {
  Particle p1;
  Particle p2;
  Joint joint;
  float length;
  float frequencyHz;
  float dampingRatio;

  Spring(Particle p1, Particle p2, float length, float frequencyHz, float dampingRatio) {
    this.p1 = p1;
    this.p2 = p2;
    this.length = length;
    this.frequencyHz = frequencyHz;
    this.dampingRatio = dampingRatio;
    makeJoint();
  }
  public void makeJoint() {
    DistanceJointDef djd = new DistanceJointDef();
    djd.bodyA = p1.body;
    djd.bodyB = p2.body;
    djd.length = box2d.scalarPixelsToWorld(length);
    djd.frequencyHz = frequencyHz;
    djd.dampingRatio = dampingRatio;
    joint = box2d.world.createJoint(djd);    
  }
  public void display() {
    Vec2 pos1 = box2d.getBodyPixelCoord(p1.body);
    Vec2 pos2 = box2d.getBodyPixelCoord(p2.body);
    stroke(255);
    strokeWeight(3);
    line(pos1.x, pos1.y, pos2.x, pos2.y);
  }
}
class Surface{
    ArrayList<Vec2> points;

    Surface(){
        points = new ArrayList();
        makeBody();
    }

    public void makeBody(){
        BodyDef bd = new BodyDef();
        bd.position.set(0,0);
        Body body = box2d.createBody(bd);

        // Crear lista de puntos en pixeles
        float offset = random(100);
        for(float x = 0; x < width; x += 5){
            float y = noise(offset) * 200 + height / 3 + x * 0.4f;

            points.add(new Vec2(x,y));
            offset += 0.03f;
        }
        float lastYPos = points.get(points.size()-1).y;
        points.add(new Vec2(width, lastYPos + 100));
        points.add(new Vec2(0, lastYPos + 100));

        // Crear lista de puntos en world
        Vec2[] worldPoints = new Vec2[points.size()];
        for (int i = 0; i < points.size(); i++){
            worldPoints[i] = box2d.coordPixelsToWorld(points.get(i));
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldPoints, worldPoints.length);

        body.createFixture(chain, 1);
    }

    public void display() {
        noStroke();
        fill(0,128,128);
        beginShape();
        for(Vec2 p : points){
            vertex(p.x,p.y);
        }
        endShape();
    }
}
  public void settings() {  fullScreen(FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ejemploRevoluteJoint" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
