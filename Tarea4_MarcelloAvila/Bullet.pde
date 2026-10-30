public class Bullet {
  PVector pos, speed, accel, gravity;
  float mass, ang, angSpeed, angAccel, lenTri, widTri;
  color bulColor;
  int MINMASS = 20;
  int MAXMASS = 30;
  boolean dead;
  
  Bullet(float x, float y, float spin) {
    dead = false;
    lenTri = map(mass,MINMASS,MAXMASS,10,45);
    widTri = map(mass,MINMASS,MAXMASS,1,5);
    
    pos = new PVector(x, y);
    speed = new PVector(0, 0);
    accel = new PVector(0, 0);
    ang = spin;
    angSpeed = 0;
    angAccel = 0;
    
    mass = random(MINMASS,MAXMASS);
    gravity = PVector.mult(gGravity, mass);
    bulColor = color(HSB,
      map(noise(TIME*2),0,1,0,255), 
      map(noise(TIME*3),0,1,0,255), 
      map(noise(TIME*4),0,1,100,255));
  }
  void draw() {
    fill(bulColor);
    noStroke();
    pushMatrix();
    translate(pos.x,pos.y);
    rotate(ang+PI);
    triangle(-widTri, lenTri/2, 0, -lenTri/2, widTri, lenTri/2);
    popMatrix();
  }
  void update() {
    applyForce(this.gravity);
    
    speed.add(accel);
    pos.add(speed);
    accel.mult(0);
    
    ang += angSpeed;
    angSpeed += angAccel;
    angAccel = map(speed.mag()/3, 0, 30, ang*0.001, -ang*0.001);
    
    outBounds();
  }
  void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    accel.add(f);
  }
  void outBounds(){
    if(pos.y - lenTri/2 > height && dead == false){
      dead = true;
      hub.update(1);
    }
  }
}