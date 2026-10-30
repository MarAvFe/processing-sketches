import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Iterator; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sistemaParticulas extends PApplet {

ParticleSystem s;
float g = 0.5f;
PVector gravity;

ArrayList<ParticleSystem> systems;

public void setup() {
  
  background(0);
  systems = new ArrayList();
  gravity = new PVector(0.3f, g);
}

public void draw() {
 // noCursor();
  background(0);

  for (ParticleSystem s : systems) {
    s.applyForce(gravity);
    s.update();
    s.draw();
    s.addParticle();
  }

  if (mousePressed){
    systems.add(new ParticleSystem(mouseX, mouseY));
  }
}
class BouncyParticle  extends Particle {

  float ang;
  float speedAng;
  float accAng;

  public BouncyParticle(float x, float y, float mass, float friction) {
    super(x, y, mass, friction);
    ang = 0;
    speedAng = 0.1f;
    accAng = 0;
  }

    public void draw(){
    noStroke();
    fill(c, lifeSpan);
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    rect(0, 0, mass, mass);
    popMatrix();
  }

  public void update(){
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
class Particle implements IAgent{
  PVector pos;
  PVector speed;
  PVector acc;
  float mass;
  int c;
  boolean fixed;
  float friction;
  float lifeSpan = 255;
  float decay = 0.5f;

  public Particle(float x, float y, float mass,float friction){
    this.pos = new PVector(x,y);
    this.speed = new PVector(0,0);
    this.acc = new PVector(0,0);
    this.c = color(255,255,255,40);
    this.mass = mass;
    this.fixed = false;
    this.friction = friction;
  }

  public void fix(){
    fixed = true;
  }

  public void unfix(){
    fixed = false;
  }

  public void calculateFriction(){
    PVector f = speed.copy();
    f.normalize();
    f.mult(-friction);
    applyForce(f);
  }

  public void update(){
    if (!this.fixed){
      calculateFriction();
      speed.add(acc);
      pos.add(speed);
      acc.mult(0);
    }
    lifeSpan -= decay;
  }



  public void draw(){
    noStroke();
    fill(c, lifeSpan);
    ellipse(pos.x,pos.y,mass * 5, mass * 5);
  }

  public void applyForce(PVector force){
    PVector f = PVector.div(force,mass);
    speed.add(f);
  }

  public boolean isDead(){
    return lifeSpan <= 0;
  }


}


class ParticleSystem {
  PVector origin;
  ArrayList<Particle> particles;

  ParticleSystem(float x, float y){
    origin = new PVector(x, y);
    particles = new ArrayList();
  }

  public void update(){
    Iterator<Particle> i = particles.iterator();
    while(i.hasNext()){
      Particle p = i.next();
      p.update();

      if(p.isDead()){
        i.remove();
      }

    }
  }

  public void draw(){
    for (Particle p : particles){
      p.draw();
    }
  }

  public void applyForce(PVector force){
    for (Particle p : particles){
      p.applyForce(force);
    }
  }

  public void addParticle(){
    Particle p;
    float ran = random(4);
    if(ran < 1) {
      p = new Particle(origin.x, origin.y, 10, 0.1f);
    }else if(ran < 2) {
      p = new SquareParticle(origin.x, origin.y, 10, 0.1f);
    }else if(ran < 3) {
      p = new SquareParticle(origin.x, origin.y, 10, 0.1f);
    }else {
      p = new BouncyParticle(origin.x, origin.y, 10, 0.1f);
     }
    PVector dir = PVector.random2D();
    dir.setMag(random(2, 5));
    p.applyForce(dir);
    particles.add(p);
  }

}
class SquareParticle  extends Particle {
  
  float ang;
  float speedAng;
  float accAng;
  
  public SquareParticle(float x, float y, float mass, float friction) {
    super(x, y, mass, friction);
    ang = 0;
    speedAng = 0.1f;
    accAng = 0;
  }
  
    public void draw(){
    noStroke();
    fill(c, lifeSpan);
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    rect(0, 0, mass, mass);
    popMatrix();
    
    
    
  }
  
  public void update(){
    super.update();
    ang += speedAng;
    speedAng += accAng;
    accAng = 0;
    
    
    
    
  }
  
}
class PairedParticle  extends Particle {

    float ang;
    float speedAng;
    float accAng;
    Particle partner;

    public PairedParticle(float x, float y, float mass, float friction, float length) {
        super(x,y,mass,friction);
        partner = new Particle(x+length,y,mass/2,friction);
        ang = 0;
        speedAng = 0.1f;
        accAng = 0;
    }

    public void draw(){
        noStroke();
        fill(c, lifeSpan);
        rectMode(CENTER);
        pushMatrix();
        partner.draw();
        stroke(255,0,0);
        line(pos.x,pos.y,partner.pos.x,partner.pos.y);
        translate(pos.x, pos.y);
        rotate(ang);
        rect(0, 0, mass, mass);
        popMatrix();
    }

    public void update(){
        super.update();
        partner.update();
        ang += speedAng;
        speedAng += accAng;
        accAng = 0;
        float randSize;
        randSize = noise(frameCount);
        mass = abs(map(randSize, 0, 1, 1, 20));
    }

    public void applyForce(PVector force){
        partner.applyForce(force);
        PVector f = PVector.div(force,mass);
        speed.add(f);
    }

}
  public void settings() {  size(800, 600, FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sistemaParticulas" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
