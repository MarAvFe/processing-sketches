class Ball {
  int BALLSIZE = 40;
  float BOUNCE = 2.28;
  float HELIUMPRESSURE = -0.035;

  float REDNOISE = 4;
  float GREENNOISE = 2;
  float BLUENOISE = 3;

  PVector pos;
  PVector speed;
  PVector acc;
  float rndR, rndG, rndB;
  color rgb;
  boolean popped = false;
  boolean standing = false;
  PVector gravity;

  Ball(float x, float y, PVector v) {
    pos = new PVector (x, y);
    speed = new PVector (v.x, v.y);
    acc = PVector.random2D();

    rgb = color(random(200), random(200), random(200), map(random(5), 0, 5, 100, 255));
  }

  void refresh() {
    if (!standing) {
      gravity = new PVector(0, 0.01);
      aplicarFuerza(gravity);
      if (!popped) {
        PVector helio = new PVector(0, HELIUMPRESSURE);
        aplicarFuerza(helio);
      }
      speed.add(acc);
      speed.limit(5);
      pos.add(speed);
      acc.mult(0);
      checkBorders();
      checkClick();
    }
  }

  void draw() {
    noStroke();
    fill(rgb);
    ellipse(pos.x, pos.y, BALLSIZE, BALLSIZE);
    if (popped) {
      stroke(255,0,0);
      line(pos.x-BALLSIZE/2,pos.y-BALLSIZE/2,pos.x+BALLSIZE/2,pos.y+BALLSIZE/2);
      line(pos.x-BALLSIZE/2,pos.y+BALLSIZE/2,pos.x+BALLSIZE/2,pos.y-BALLSIZE/2);
    }
  }

  void aplicarFuerza(PVector fuerza) {
    acc.add(fuerza);
  }

  void checkBorders() {
    if (pos.x + BALLSIZE/2 > width) {
      speed = speed.mult(0.8);
      aplicarFuerza(new PVector(-speed.x * BOUNCE, 0));
    }
    if (pos.x - BALLSIZE/2 < 0) {
      speed = speed.mult(0.8);
      aplicarFuerza(new PVector(-speed.x * BOUNCE, 0));
    }
    if (!popped) {
      if (pos.y + BALLSIZE/2 > height) {
        speed = speed.mult(0.8);
        aplicarFuerza(new PVector(0, -speed.y * BOUNCE));
      }
      if (pos.y - BALLSIZE/2 < 0) {
        speed = speed.mult(0.8);
        aplicarFuerza(new PVector(0, -speed.y * BOUNCE));
      }
    } else {
      if (pos.y + BALLSIZE/2 > height) {
        acc.mult(0);
        speed = speed.mult(0);
        standing = true;
      }
    }
  }

  void checkClick() {
    // Revisa si el mouse clickeó el globo
    if (mousePressed) {
      if (
        (abs(mouseX-pos.x) < BALLSIZE/2) && 
        (abs(mouseY-pos.y) < BALLSIZE/2))
      {
        popped = true;
        aplicarFuerza(new PVector(0, 1));
      }
    }
  }
}