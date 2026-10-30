import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ejemploMultipleShape extends PApplet {






Box2DProcessing box2d;
ArrayList<MultipleShape> polygons;
Surface surface;

public void setup() {
  
  box2dInit();
}
public void box2dInit() {
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.setGravity(0, -20);
  polygons = new ArrayList();
  surface = new Surface();
}
public void draw() {
  background(0);
  box2d.step();

  surface.display();
  for (MultipleShape p : polygons) {
    p.display();
  }
  for (int i = polygons.size() - 1; i >= 0; i--) {
    MultipleShape p = polygons.get(i);
    if (p.done()) {
      polygons.remove(i);
    }
  }
  if (mousePressed) {
    polygons.add(new MultipleShape(mouseX, mouseY));
  }
}
class MultipleShape {
  Body body;
  float w, h, r;
  int c1, c2, c3;

  MultipleShape(float x, float y) {
    w = 10;
    h = 40;
    r = 5;
    makeBody(x, y);
    c1 = color(random(255), random(255), random(255));
    c2 = color(random(255), random(255), random(255));
    c3 = c2;
  }
  public void makeBody(float x, float y) {
    BodyDef bd = new BodyDef();
    bd.setType(BodyType.DYNAMIC);
    bd.setPosition(box2d.coordPixelsToWorld(x, y));
    bd.setAngle(random(PI));
    body = box2d.createBody(bd);

    // crear el mango martillo
    PolygonShape mango = new PolygonShape();
    float box2dW = box2d.scalarPixelsToWorld(w);
    float box2dH = box2d.scalarPixelsToWorld(h);
    mango.setAsBox(box2dW, box2dH);

    FixtureDef mangoFixture = new FixtureDef();
    mangoFixture.setShape(mango);
    mangoFixture.setDensity(1);
    mangoFixture.setRestitution(0.5f);
    mangoFixture.setFriction(0.2f);

    body.createFixture(mangoFixture);

    // crear cabeza martillo
    PolygonShape rectangle = new PolygonShape();
    box2dW = box2d.scalarPixelsToWorld(w / 2);
    box2dH = box2d.scalarPixelsToWorld(h / 2);
    rectangle.setAsBox(box2dW, box2dH, new Vec2(bd.getPosition().x,bd.getPosition().y), 45);

    FixtureDef rectangleFixture = new FixtureDef();
    rectangleFixture.setShape(rectangle);
    rectangleFixture.setDensity(1);
    rectangleFixture.setRestitution(0.5f);
    rectangleFixture.setFriction(0.2f);

    body.createFixture(rectangleFixture);

    // crear primer c\u00edrculo
    CircleShape circle1 = new CircleShape();
    circle1.setRadius(box2d.scalarPixelsToWorld(r));
    Vec2 offset1 = box2d.vectorPixelsToWorld(-w / 2 - r, 0);
    circle1.m_p.set(offset1.x, offset1.y);

    FixtureDef circle1Fixture = new FixtureDef();
    circle1Fixture.setShape(circle1);
    circle1Fixture.setDensity(1);
    circle1Fixture.setRestitution(0.5f);
    circle1Fixture.setFriction(0.2f);

    body.createFixture(circle1Fixture);

    // crear segundo c\u00edrculo
    CircleShape circle2 = new CircleShape();
    circle2.setRadius(box2d.scalarPixelsToWorld(r));
    Vec2 offset2 = box2d.vectorPixelsToWorld(w / 2 + r, 0);
    circle2.m_p.set(offset2.x, offset2.y);

    FixtureDef circle2Fixture = new FixtureDef();
    circle2Fixture.setShape(circle2);
    circle2Fixture.setDensity(1);
    circle2Fixture.setRestitution(0.5f);
    circle2Fixture.setFriction(0.2f);

    body.createFixture(circle2Fixture);

    body.setLinearVelocity(new Vec2(random(-10, 10), random(-5, 5)));
    body.setAngularVelocity(random(-10, 10));
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
  public void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();

    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    noStroke();
    fill(c1);
    rect(0, 0, w, h);
    fill(c2);
    rotate(45);
    rect(0, 0, w*2, h*2);
    rotate(-45);

    ellipse(-w / 2 - r, 0, r*2, r*2);
    fill(c3);
    ellipse(w / 2 + r, 0, r*2, r*2);
    popMatrix();
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
    BodyDef bd = new BodyDef();
    bd.setPosition(box2d.coordPixelsToWorld(x, y));
    bd.setType(BodyType.DYNAMIC);
    bd.setAngle(random(PI));
    body = box2d.createBody(bd);

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
class Surface {
  ArrayList<Vec2> points;

  Surface() {
    points = new ArrayList();
    makeBody();
  }
  public void makeBody() {
    BodyDef bd = new BodyDef();
    bd.position.set(0, 0); // la superficie no necesita posici\u00f3n
    Body body = box2d.createBody(bd); // no es necesario guardar la referencia a body porque nunca se va a mover

    // Crear lista de puntos en pixeles
    ChainShape chain = new ChainShape();
    for (float x = 0; x < width/2; x += 100) {
      float y = height * 2/3 + random(40);
      points.add(new Vec2(x, y));
    }
    float lastYPos = points.get(points.size() - 1).y;
    float lastXPos = points.get(points.size() - 1).x;
    points.add(new Vec2(lastXPos, lastYPos + 100));
    points.add(new Vec2(0, lastYPos + 100));
    // Crear lista de puntos en world
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
  public void settings() {  size(800,600,FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ejemploMultipleShape" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
