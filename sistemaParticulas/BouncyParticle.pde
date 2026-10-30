class BouncyParticle  extends Particle {

  float ang;
  float speedAng;
  float accAng;

  public BouncyParticle(float x, float y, float mass, float friction) {
    super(x, y, mass, friction);
    ang = 0;
    speedAng = 0.1;
    accAng = 0;
  }

    void draw(){
    noStroke();
    fill(c, lifeSpan);
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    rect(0, 0, mass, mass);
    popMatrix();
  }

  void update(){
    super.update();
    ang += speedAng;
    speedAng += accAng;
    accAng = 0;
    pos.x = pos.x%width;
    pos.y = pos.y%height;
    float randSize;
    randSize = noise(frameCount);
    mass = abs(map(randSize, 0, 1, 1, 20));
  }

}
