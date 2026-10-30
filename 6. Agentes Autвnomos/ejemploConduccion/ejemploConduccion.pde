Vehicle v;

void setup() {
  size(800, 600);
  v = new Vehicle(width / 2, height / 2, PVector.random2D());
  background(0);
}

void draw() {
  background(0);
  PVector mouse = new PVector(mouseX, mouseY);
  v.seek(mouse);
  v.update();
  v.display();
}