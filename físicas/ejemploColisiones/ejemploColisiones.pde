import java.util.Iterator;
import shiffman.box2d.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

Box2DProcessing box2d;

ArrayList<Particle> particles;

Surface surface;

void setup() {
  fullScreen(FX2D);
  box2dInit();
}
void box2dInit() {
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.listenForCollisions();
  particles = new ArrayList();
  surface = new Surface();
}

void draw() {
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
void beginContact(Contact c) {
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
void endContact(Contact c) {
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
