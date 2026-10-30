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

public class ejemploDistanceJoint extends PApplet {








Box2DProcessing box2d;

Surface surface;
ArrayList<Particle> particles;
ArrayList<Spring> springs;

public void setup() {
  //size(800, 600);
  
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  ArrayList<Vec2> points = new ArrayList();
  points.add(new Vec2(width / 2 - 250, height * 0.8f));
  points.add(new Vec2(width / 2 + 250, height * 0.8f));
  points.add(new Vec2(width / 2 + 250, height * 0.85f));
  points.add(new Vec2(width / 2 - 250, height * 0.85f));
  surface = new Surface(points);
  particles = new ArrayList();
  springs = new ArrayList();
}

public void draw() {
  background(0);
  box2d.step();
  surface.display();
  for (Particle p : particles) {
    p.display();
  }
  for (Spring s : springs) {
    s.display();
  }
}

public void mousePressed() {
  Particle p1 = new Particle(mouseX + random(-5, 5), mouseY + random(-5, 5), random(50));
  Particle p2 = new Particle(mouseX + random(-5, 5), mouseY + random(-5, 5), random(50));
  Spring s = new Spring(p1, p2, random(40, 70), random(0.1f, 10), random(1));
  particles.add(p1);
  particles.add(p2);
  springs.add(s);
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
    fd.setFriction(0.01f);
    fd.setRestitution(0.8f);
    
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

    vertices.add(new Vec2(0, -20));
    vertices.add(new Vec2(14, 0));
    vertices.add(new Vec2(8, 20));
    vertices.add(new Vec2(-8, 20));
    vertices.add(new Vec2(-14, 0));

    makeBody(x, y);
    c = color(random(255), random(255), random(255));
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
    bd.setAngle(random(PI));
    body = box2d.createBody(bd);
    body.createFixture(fd);
    body.setLinearVelocity(new Vec2(random(-5, 5), random(2, 5)));
    body.setAngularVelocity(random(-5, 5));
  }

  public void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    PolygonShape ps = (PolygonShape) body.getFixtureList().getShape();
    float a = body.getAngle();
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    fill(c);
    noStroke();
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
class Surface {
  ArrayList<Vec2> points;

  Surface(ArrayList<Vec2> points) {
    this.points = points;
    makeBody();
  }
  public void makeBody() {
    BodyDef bd = new BodyDef();
    bd.position.set(0, 0); // la superficie no necesita posici\u00f3n
    Body body = box2d.createBody(bd); // no es necesario guardar la referencia a body porque nunca se va a mover

    // Crear lista de puntos en pixeles
    ChainShape chain = new ChainShape();
    Vec2[] worldPoints = new Vec2[points.size()];
    for (int i = 0; i < points.size(); i++) {
      worldPoints[i] = box2d.coordPixelsToWorld(points.get(i));
    }
    
    chain.createChain(worldPoints, worldPoints.length);

    // Se pueden crear fixtures directamente a partir de una forma
    // El par\u00e1metro 1 es la densidad
    body.createFixture(chain, 1);
  }
  public void display() {
    noStroke();
    fill(255);
    beginShape();
    for (Vec2 p : points) {
      vertex(p.x, p.y);
    }
    endShape();
  }
}
  public void settings() {  fullScreen(FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ejemploDistanceJoint" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
