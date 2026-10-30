class Canon {

  Water water;
  float angule;
  PVector base, course;
  Bullet[] bullets;

  Canon(Water w) {
    water = w;
    base = new PVector(width/2, water.watTopLevel+water.watLevel);
    course = new PVector(mouseX, mouseY);
    bullets = new Bullet[numBullets];
  }

  void draw() {
    pushStyle();
    pushMatrix();
    noStroke();
    rectMode(CORNERS);
    translate(base.x, base.y);
    fill(100, 70, 30);
    rect(-width*0.05, -10, width*0.05, 10, 0, 0, 15, 15);
    fill(50);
    rotate(angule);
    rect(-10, -80, 10, 5, 0, 0, 15, 15);
    fill(0);
    ellipse(0, 0, 5, 5);
    popMatrix();
    popStyle();
  }

  void update() {
    base.x = width/2;
    base.y = water.watTopLevel+water.watLevel;
    course = new PVector(mouseX, mouseY);

    angule = -atan((course.x-base.x)/(course.y-base.y));
    if (base.y < course.y) {
      angule += PI;
    }

    if (mousePressed) {
      int fs = getFreeSpace();
      if (fs > -1) {
        PVector dif = PVector.sub(course, base);
        bullets[fs] = new Bullet(base.x, base.y, angule);
        dif.setMag(base.y*0.65);
        bullets[fs].applyForce(dif);
        hub.update(-1);
      }
    }

    for (Bullet b : bullets) {
      if (b != null) {
        // Water drag
        if (water.contains(b)) {
          b.applyForce(water.drag(b));
        } else {
          // Wind friction
          PVector friction = b.speed.copy();
          friction.normalize();
          friction.mult(WINDFRICTIONCOEF);
          b.applyForce(friction);
        };
        b.draw();
        b.update();
      }
    }
  }

  int getFreeSpace() {
    for (int i = 0; i < bullets.length; i++) if (bullets[i] == null || bullets[i].dead) return i;
    return -1;
  };
}