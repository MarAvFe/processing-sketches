import java.util.Iterator;
import shiffman.box2d.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;

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
  particles = new ArrayList();
  surface = new Surface();
}

void draw() {
  background(0);
  if (mousePressed) {
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
