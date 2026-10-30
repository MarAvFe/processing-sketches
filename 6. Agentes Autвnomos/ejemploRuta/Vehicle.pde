class Vehicle {
  PVector pos;
  PVector vel;
  PVector acc;
  float mass = 1;
  float r = 5;
  float maxSpeed;
  float maxForce;
  float lookAhead = 50;
  color c;
  boolean debug = true;
  
  Vehicle(float x, float y, PVector vel, float maxSpeed, float maxForce) {
    pos = new PVector(x, y);
    this.vel = vel;
    acc = new PVector(0, 0);
    this.maxSpeed = maxSpeed;
    this.maxForce = maxForce;
    c = color(random(100, 255), 58, random(0, 255));
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
  void borders() {
    pos.x = (pos.x + width) % width;
    pos.y = (pos.y + height) % height;
  }
  void follow(Path path) {
    PVector predictedPos = getPredictedPos();
    PVector normalPos = getNormalPos(predictedPos, path);
    float distance = PVector.dist(predictedPos, normalPos);
    if (distance > path.radius) {
      seek(normalPos);
    }
    if (debug) {
      noStroke();
      fill(0, 0, 255);
      ellipse(predictedPos.x, predictedPos.y, 5, 5);
      fill(255, 0, 0);
      ellipse(normalPos.x, normalPos.y, 5, 5);
    }
  }
  PVector getPredictedPos() {
    PVector predicted = vel.copy();
    predicted.setMag(lookAhead);
    predicted.add(pos);
    return predicted;
  }
  PVector getNormalPos(PVector pos, Path path) {
    PVector a = PVector.sub(pos, path.start);
    PVector b = PVector.sub(path.end, path.start);
    b.normalize();
    b.mult(a.dot(b));
    b.add(path.start);
    return b;
  }
}