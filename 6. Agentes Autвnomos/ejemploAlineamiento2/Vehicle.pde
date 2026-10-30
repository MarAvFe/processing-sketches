class Vehicle {
  PVector pos;
  PVector vel;
  PVector acc;
  float mass = 1;
  float r = 5;
  float maxSpeed;
  float maxForce;
  float alignmentDistance;

  color c;
  boolean debug = true;

  Vehicle(float x, float y, PVector vel, float maxSpeed, float maxForce) {
    pos = new PVector(x, y);
    this.vel = vel;
    acc = new PVector(0, 0);
    this.maxSpeed = maxSpeed;
    this.maxForce = maxForce;
    alignmentDistance = 50;
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
  void align(ArrayList<Vehicle> vehicles) {
    PVector average = new PVector(0, 0);
    int count = 0;
    for (Vehicle v : vehicles) {
      float d = PVector.dist(pos, v.pos);
      if (d < alignmentDistance) {
        average.add(v.vel);
        count++;
      }
    }
    if (count > 0) {
      average.div(count);
      //average.sub(vel);
      average.limit(maxForce);
      applyForce(average);
    }
  }
}