ParticleSystem s;
float g = 0.5;
PVector gravity;

ArrayList<ParticleSystem> systems;

void setup() {
  size(800, 600, FX2D);
  background(0);
  systems = new ArrayList();
  gravity = new PVector(0.3, g);
}

void draw() {
 // noCursor();
  background(0);

  for (ParticleSystem s : systems) {
    s.applyForce(gravity);
    s.update();
    s.draw();
    s.addParticle();
  }

  if (mousePressed){
    systems.add(new ParticleSystem(mouseX, mouseY));
  }
}
