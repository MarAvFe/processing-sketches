import shiffman.box2d.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;

Box2DProcessing box2d;
ArrayList<Box> boxes;
NoiseSurface terrain;

void setup() {
  fullScreen(FX2D, 2);
  background(0);
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  boxes = new ArrayList();
  terrain = new NoiseSurface(color(255), height * 0.60, 300, 0.1, 10);
}

void draw() {
  background(0);
  box2d.step();
  terrain.draw();
  for (Box b : boxes) {
    b.draw();
  }
  if (mousePressed) {
    Box b = new Box(mouseX, mouseY, color(255, 0, 0), 25, 25, random(PI), 1, 0.1, 0.5);
    boxes.add(b);
  }
}