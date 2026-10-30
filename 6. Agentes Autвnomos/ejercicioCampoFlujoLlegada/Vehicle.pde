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
  boolean wrapBorders;

  Vehicle(float x, float y, PVector vel) {
    pos = new PVector(x, y);
    this.vel = vel;
    acc = new PVector(0, 0);
    mass = 1;
    r = 4;
    maxSpeed = 10;
    maxForce = 0.05;
    c = color(128, 58, 250);
    arrivalRadius = 200;
    wrapBorders = false;
  }
  void update() {
    vel.add(acc);
    vel.limit(maxSpeed);
    pos.add(vel);
    acc.mult(0);
    borders();
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
    float d = desired.mag();
    d = constrain(d, 0, arrivalRadius);
    float speed = map(d, 0, arrivalRadius, 0, maxSpeed);
    desired.setMag(speed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);
  }
  void follow(PVector desired) {
    desired.setMag(maxSpeed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);
  }
  void display() {
    float ang = vel.heading();
    stroke(0);
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
  void setWrapBorders(boolean wrap) {
    wrapBorders = wrap;
  }
  void borders() {
    if (wrapBorders) {
      pos.x = (pos.x + width) % width;
      pos.y = (pos.y + height) % height;
    }
  }
}