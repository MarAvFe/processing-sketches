Ball b;

void setup() {
  size(800, 600);
  b = new Ball(width/2, height/2, 0, 0);
}

void draw() {
  background(0);
  PVector gravity = new PVector(0, 0.5);
  b.aplicarFuerza(gravity);

  if (mousePressed) {
    PVector viento = new PVector(mouseX, mouseY);
    viento.sub(b.pos);
    viento.setMag(-1);
    b.aplicarFuerza(viento);
  }

  b.draw();
  b.refresh();
}