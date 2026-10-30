class Particle implements IAgent{
  PVector pos;
  PVector speed;
  PVector acc;
  float mass;
  color c;
  boolean fixed;
  float friction;
  float lifeSpan = 255;
  float decay = 0.5;

  public Particle(float x, float y, float mass,float friction){
    this.pos = new PVector(x,y);
    this.speed = new PVector(0,0);
    this.acc = new PVector(0,0);
    this.c = color(255,255,255,40);
    this.mass = mass;
    this.fixed = false;
    this.friction = friction;
  }

  void fix(){
    fixed = true;
  }

  void unfix(){
    fixed = false;
  }

  void calculateFriction(){
    PVector f = speed.copy();
    f.normalize();
    f.mult(-friction);
    applyForce(f);
  }

  void update(){
    if (!this.fixed){
      calculateFriction();
      speed.add(acc);
      pos.add(speed);
      acc.mult(0);
    }
    lifeSpan -= decay;
  }



  void draw(){
    noStroke();
    fill(c, lifeSpan);
    ellipse(pos.x,pos.y,mass * 5, mass * 5);
  }

  void applyForce(PVector force){
    PVector f = PVector.div(force,mass);
    speed.add(f);
  }

  boolean isDead(){
    return lifeSpan <= 0;
  }


}
