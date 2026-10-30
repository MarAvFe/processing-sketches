class Vehicle {
  PVector pos;
  PVector vel;
  PVector acc;
  float mass;
  float size;
  float maxSpeed;
  float maxForce;
  color c;
  float arrivalRadius;

  Vehicle(float x, float y, PVector vel) {
    pos = new PVector(x, y);
    this.vel = vel;
    acc = new PVector(0, 0);
    mass = 1;
    size = 10;
    maxSpeed = 4;
    maxForce = 0.01;
    c = color(128, 58, 250);
    arrivalRadius = 200;
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

  void display() {
    float ang = vel.heading();
    //stroke(255);
    //strokeWeight(3);
    fill(c,20);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    beginShape();
    vertex(size * 3, 0);
    vertex(0, -size);
    vertex(0, size);
    endShape(CLOSE);
    fill(255,0,0,2zz0);
    ellipse(size*3,0,3,3);
    popMatrix();
  }
  void seek(PVector target){
    PVector desired = PVector.sub(target, pos);
    desired.setMag(maxSpeed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);
  }

  void arrive(PVector target){
    PVector desired = PVector.sub(target, pos);
    float d = PVector.dist(pos, target);
    d = constrain(d, 0, arrivalRadius);
    float speed = map(d, 0, arrivalRadius, 0, maxSpeed);
    vel.setMag(speed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);

  }
  void borders(){
      pos.x = (pos.x + width)%width;
      pos.y = (pos.y + height)%height;
  }

}
