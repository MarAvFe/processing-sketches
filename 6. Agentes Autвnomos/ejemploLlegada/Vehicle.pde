class Vehicle {
  PVector pos;
  PVector vel;
  PVector acc;
  float mass;
  float maxSpeed;
  float maxForce;
  float arrivalRadius;

  float r;
  color c;

  Vehicle(float x, float y, PVector vel) {
    pos = new PVector(x, y);
    this.vel = vel;
    acc = new PVector(0, 0);
    mass = 1;
    r = 10;
    maxSpeed = 4;
    maxForce = 0.3;
    c = color(128, 58, 250);
    arrivalRadius = 200;
  }
  void update() {
    vel.add(acc);
    vel.limit(maxSpeed);
    pos.add(vel);
    acc.mult(0);
  }
  void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    acc.add(f);
  }
  void seek(PVector target) {
    PVector desired = PVector.sub(target, pos);
    desired.setMag(maxSpeed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);
  }
  void arrive(PVector target) {
    PVector desired = PVector.sub(target, pos);
    float d = PVector.dist(pos, target);
    d = constrain(d, 0, arrivalRadius);
    float speed = map(d, 0, arrivalRadius, 0, maxSpeed);
    vel.setMag(speed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);
  }
  void display() {
    float ang = vel.heading();
    stroke(255);
    strokeWeight(2);
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    beginShape();
    vertex(r * 3, 0);
    vertex(0, -r);
    vertex(0, r);
    endShape(CLOSE);
    popMatrix();
  }
}