class Vehicle {
  PVector pos;
  PVector vel;
  PVector acc;
  float mass = 1;
  float r = 3;
  float maxSpeed;
  float maxForce;
  float alignmentDistance;
  float alignmentRatio;
  float separationDistance;
  float separationRatio;
  float cohesionDistance;
  float cohesionRatio;

  color c;
  boolean debug = false;

  Vehicle(float x, float y, PVector vel, float maxSpeed, float maxForce) {
    pos = new PVector(x, y);
    this.vel = vel;
    acc = new PVector(0, 0);
    this.maxSpeed = maxSpeed;
    this.maxForce = maxForce;
    separationDistance = 50;
    separationRatio = 1;
    alignmentDistance = 70;
    alignmentRatio = 1;
    cohesionDistance = 70;
    cohesionRatio = 1;
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
    if (debug) {
      noFill();
      strokeWeight(1);
      stroke(255, 0, 0, 100);
      ellipse(0, 0, cohesionDistance * 2, cohesionDistance * 2);
      stroke(0, 255, 0, 100);
      ellipse(0, 0, alignmentDistance * 2, alignmentDistance * 2);
      stroke(128, 128, 255, 100);
      ellipse(0, 0, separationDistance * 2, separationDistance * 2);      
    }
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
      if (this != v && d < alignmentDistance) {
        average.add(v.vel);
        count++;
      }
    }
    if (count > 0) {
      average.div(count);
      average.setMag(alignmentRatio);
      average.limit(maxForce);
      applyForce(average);
    }
  }
  void separate(ArrayList<Vehicle> vehicles) {
    PVector average = new PVector(0, 0);
    int count = 0;
    for (Vehicle v : vehicles) {
      float d = PVector.dist(pos, v.pos);
      if (this != v && d < separationDistance) {
        PVector difference = PVector.sub(pos, v.pos);
        difference.normalize();
        difference.div(d);
        average.add(difference);
        count++;
      }
    }
    if (count > 0) {
      average.div(count);
      average.setMag(separationRatio);
      average.limit(maxForce);
      applyForce(average);
    }
  }
  void cohere(ArrayList<Vehicle> vehicles) {
    PVector center = new PVector(0, 0);
    int count = 0;
    for (Vehicle v : vehicles) {
      float d = PVector.dist(pos, v.pos);
      if (this != v && d < cohesionDistance) {
        center.add(v.pos);
        count++;
      }
    }
    if (count > 0) {
      center.div(count);
      PVector force = center.sub(pos);
      force.setMag(cohesionRatio);
      force.limit(maxForce);
      applyForce(force);
    }
  }
  void flock(ArrayList<Vehicle> vehicles) {
    separate(vehicles);
    align(vehicles);
    cohere(vehicles);
  }
}