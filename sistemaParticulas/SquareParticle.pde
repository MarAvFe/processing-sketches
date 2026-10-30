class SquareParticle  extends Particle {
  
  float ang;
  float speedAng;
  float accAng;
  
  public SquareParticle(float x, float y, float mass, float friction) {
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
    
    
    
    
  }
  
}