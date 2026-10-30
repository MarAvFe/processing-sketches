class Ball {
  PVector pos;
  PVector speed;
  PVector acc;

  Ball(float x, float y, float speedX, float speedY) {
    pos = new PVector (x, y);
    speed = new PVector (speedX, speedY);
    acc = new PVector(0, 0);
  }

  void draw() {
    noStroke();
    fill(255);
    ellipse(pos.x, pos.y, 40, 40);
  }

  void refresh() {
    speed.add(acc);
    speed.limit(5);
    pos.add(speed);
  }
}