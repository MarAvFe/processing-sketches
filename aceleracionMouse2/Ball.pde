class Ball {
  PVector pos;
  PVector speed;
  PVector acc;

  Ball(float x, float y, float speedX, float speedY) {
    pos = new PVector (x, y);
    speed = new PVector (speedX, speedY);
    acc = new PVector(0, 0);
  }

  void refresh() {
    speed.add(acc);
    speed.limit(5);
    pos.add(speed);
    acc.mult(0);
    pos.x  = pos.x % width;
    pos.y = pos.y % height;
  }

  void draw() {
    noStroke();
    fill(255);
    ellipse(pos.x, pos.y, 40, 40);
  }

  void aplicarFuerza(PVector fuerza) {
    acc.add(fuerza);
  }
}
