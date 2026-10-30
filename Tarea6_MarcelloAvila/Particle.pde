class Particle implements IAgent {
  PVector pos;
  PVector speed;
  PVector acc;
  float mass;
  color c;
  boolean fixed;
  float friction;
  float lifeSpan;
  float decay = 0.5;

  public Particle(float x, float y, float mass, float friction, color colr, float lifeSpan) {
    this.pos = new PVector(x, y);
    this.speed = new PVector(0, 0);
    this.acc = new PVector(0, 0);
    this.c = colr;
    this.mass = mass;
    this.fixed = false;
    this.friction = friction;
    this.lifeSpan = lifeSpan;
  }

  void fix() {
    fixed = true;
  }

  void unfix() {
    fixed = false;
  }

  void calculateFriction() {
    PVector f = speed.copy();
    f.normalize();
    f.mult(-friction);
    applyForce(f);
  }

  void update() {
    if (!this.fixed) {
      calculateFriction();
      speed.add(acc);
      pos.add(speed);
      acc.mult(0);
    }
    lifeSpan -= decay;

    for (Bola p : pullers) {
      PVector r = PVector.sub(p.posicion, this.pos);
      float d = r.magSq();
      d = constrain(d, 1, 200);
      r.normalize();
      int dir = 1;
      if(p.pull) dir = -1;
      r.mult(dir * G * p.masa * this.mass);
      r.div(d);
      this.applyForce(r);
    }
  }

  void draw() {
    noStroke();
    fill(c, this.lifeSpan);
    ellipse(pos.x, pos.y, mass*2, mass * 2);
  }

  void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    speed.add(f);
  }

  boolean isDead() {
    return this.lifeSpan <= 0;
  }
}