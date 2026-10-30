import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Iterator; 
import shiffman.box2d.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.contacts.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ejemploColisiones extends PApplet {








Box2DProcessing box2d;

ArrayList<Particle> particles;

Surface surface;

public void setup() {
  
  box2dInit();
}
public void box2dInit() {
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.listenForCollisions();
  particles = new ArrayList();
  surface = new Surface();
}

public void draw() {
  background(0);
  if (mousePressed && frameCount % 20 == 0) {
    float size = random(10, 20);
    particles.add(new Particle(mouseX, mouseY, size));
  }

  box2d.step();
  surface.display();
  for (Particle p : particles) {
    p.display();
  }
  for (int i = particles.size() - 1; i >= 0; i--) {
    Particle p = particles.get(i);
    if (p.done()) {
      particles.remove(i);
    }
  }
}
public void beginContact(Contact c) {
  Fixture f1 = c.getFixtureA();
  Fixture f2 = c.getFixtureB();
  Body b1 = f1.getBody();
  Body b2 = f2.getBody();
  Object o1 = b1.getUserData();
  Object o2 = b2.getUserData();
  if (o1 instanceof Particle) {
    Particle p1 = (Particle) o1;
    p1.changeColor();
  }
  if (o2 instanceof Particle) {
    Particle p2 = (Particle) o2;
    p2.changeColor();
  }
}
public void endContact(Contact c) {
  Fixture f1 = c.getFixtureA();
  Fixture f2 = c.getFixtureB();
  Body b1 = f1.getBody();
  Body b2 = f2.getBody();
  Object o1 = b1.getUserData();
  Object o2 = b2.getUserData();
  if (o1 instanceof Particle) {
    Particle p1 = (Particle) o1;
    p1.revertColor();
  }
  if (o2 instanceof Particle) {
    Particle p2 = (Particle) o2;
    p2.revertColor();
  }
}
class Particle {
  Body body;
  float r;
  int c;
  
  Particle(float x, float y, float r) {
    this.r = r;
    c = color(0xffFF0062);
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
    fd.setRestitution(0.3f);
    
    body.createFixture(fd);
    
    body.setLinearVelocity(new Vec2(random(-10, 10), random(0, 10)));
    body.setAngularVelocity(random(-10, 10));
    body.setUserData(this);
  }
  public void killBody() {
    box2d.destroyBody(body);
  }
  public boolean done() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    if (pos.y > height + r*2) {
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
    fill(c);
    rotate(-a);
    stroke(255);
    strokeWeight(2);
    ellipse(0, 0, r * 2, r * 2);
    line(-r, 0, r, 0);
    line(0, -r, 0, r);
    popMatrix();
  }
  public void changeColor() {
    c = color(0xff6281D3);
  }
  public void revertColor() {
    c = color(0xff880062);
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
    float offset = random(100);
    for (float x = 0; x < width; x += 5) {
      float y = noise(offset) * 200 + height / 3 + x * 0.4f;
      points.add(new Vec2(x, y));
      offset += 0.03f;
    }
    float lastYPos = points.get(points.size() - 1).y;
    points.add(new Vec2(width, lastYPos + 100));
    points.add(new Vec2(0, lastYPos + 100));
    // Crear lista de puntos en world
    Vec2[] worldPoints = new Vec2[points.size()];
    for (int i = 0; i < points.size(); i++) {
      worldPoints[i] = box2d.coordPixelsToWorld(points.get(i));
    }
    ChainShape chain = new ChainShape();
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
    String[] appletArgs = new String[] { "ejemploColisiones" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
