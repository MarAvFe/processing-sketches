import shiffman.box2d.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.dynamics.joints.*;

Box2DProcessing box2d;

Surface surface;
ArrayList<Particle> particles;
ArrayList<Spring> springs;

void setup() {
  //size(800, 600);
  fullScreen(FX2D);
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  ArrayList<Vec2> points = new ArrayList();
  points.add(new Vec2(width / 2 - 250, height * 0.8));
  points.add(new Vec2(width / 2 + 250, height * 0.8));
  points.add(new Vec2(width / 2 + 250, height * 0.85));
  points.add(new Vec2(width / 2 - 250, height * 0.85));
  surface = new Surface(points);
  particles = new ArrayList();
  springs = new ArrayList();
}

void draw() {
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

void mousePressed() {
  Particle p1 = new Particle(mouseX + random(-5, 5), mouseY + random(-5, 5), random(50));
  Particle p2 = new Particle(mouseX + random(-5, 5), mouseY + random(-5, 5), random(50));
  Spring s = new Spring(p1, p2, random(40, 70), random(0.1, 10), random(1));
  particles.add(p1);
  particles.add(p2);
  springs.add(s);
}
