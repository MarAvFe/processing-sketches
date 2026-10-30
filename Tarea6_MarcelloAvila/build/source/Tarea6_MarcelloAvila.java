import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import java.util.Iterator; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Tarea6_MarcelloAvila extends PApplet {

/*
 Simulaci\u00f3n de Sistemas Naturales
 Marcello \u00c1vila
 2014084118

 Tarea 6 - Resortes atractivos

 Los c\u00edrculos azules repelen y los rojos atraen.
   Se generan aleatoriamente.
 Con click derecho se agregan nuevo sistemas de part\u00edculas para
   evitar conflicto al momento de manipular los controles.
 Por alguna raz\u00f3n no pude inicializar bien los valores de algunos
   de los controles, entonces aparecen con n\u00fameros muy peque\u00f1os.
   Algunos de los controles van de 1-100, pero a la hora de aplicar
   la funci\u00f3n, tienen un map, por lo que no son los valores reflejados
   en la interfaz. Sin ninguna raz\u00f3n particular.

*/

ControlP5 cp5;

ParticleSystem s;
float g = 0.002f;
PVector gravity;
float friction = 0.02f;
float k = 0.01f;
float lifeSpan = 300;
float particleMass = 10;
float restLen = 100;

float G = 0.05f;

ArrayList<ParticleSystem> systems;
Bola[] pullers;
int NUMPULLERS = 25;

public void setup() {
  
  background(0);
  systems = new ArrayList();
  gravity = new PVector(0, g);
  pullers = new Bola[NUMPULLERS];
  for (int i = 0; i < pullers.length; i++) {
    pullers[i] = new Bola(random(0, width), random(0, height), random(2)>1?true:false);
  }
  setGravity(g);
  setFriction(friction);
  setParticleMass(particleMass);
  initControl();
}

public void draw() {
  fill(0, 0, 0, 30);
  noStroke();
  rect(0, 0, width, height);
  for (ParticleSystem s : systems) {
    s.applyForce(gravity);
    s.update();
    s.draw();
    if (frameCount % 20 == 0) s.addParticle();
  }
  for (Bola p : pullers) {
    p.draw();
    p.update();
  }

  if (mousePressed && (mouseButton == RIGHT)) {
    systems.add(new ParticleSystem(mouseX, mouseY));
  }
}

public void setGravity(float value) {
  gravity = new PVector(0, value);
}

public void setFriction(float value) {
  friction = value;
}

public void setParticleMass(float value) {
  particleMass = map(value,0,100,2,30);
}

public void setK(float value) {
  k = map(value,0,100,0.005f,1.2f);
}

public void setLife(float value) {
  lifeSpan = map(value,0,100,30,500);
}

public void setRestLen(float value) {
  restLen = map(value,0,100,2,30);
}

public void initControl() {
  cp5 = new ControlP5(this);
  cp5.addSlider("setGravity")
    .setPosition(10, 10)
    .setSize(200, 20)
    .setRange(0, 0.05f)
    .setValue(g)
    .setCaptionLabel("Gravedad");

  cp5.addSlider("setFriction")
    .setPosition(10, 40)
    .setSize(200, 20)
    .setRange(0, 0.5f)
    .setValue(friction)
    .setCaptionLabel("Friccion");

  cp5.addSlider("setParticleMass")
    .setPosition(10, 70)
    .setSize(200, 20)
    .setRange(1, 100)
    .setValue(particleMass)
    .setCaptionLabel("Masa");

  cp5.addSlider("setK")
    .setPosition(10, 100)
    .setSize(200, 20)
    .setRange(0, 2)
    .setValue(k)
    .setCaptionLabel("Elasticidad");

  cp5.addSlider("setLife")
    .setPosition(10, 130)
    .setSize(200, 20)
    .setRange(1, 100)
    .setValue(lifeSpan)
    .setCaptionLabel("Vida");

  cp5.addSlider("setRestLen")
    .setPosition(10, 160)
    .setSize(200, 20)
    .setRange(1, 100)
    .setValue(restLen)
    .setCaptionLabel("Longitud");
}
public class Bola {
  PVector posicion;
  PVector velocidad;
  PVector aceleracion;
  float masa, rndR, rndG, rndB;
  boolean pull;
  int colr;
  Bola(float x, float y, boolean pull) {
    posicion = new PVector(x, y);
    velocidad = new PVector(0, 0);
    aceleracion = new PVector(0, 0);
    this.pull = pull;
    if(this.pull){
        rndR = random(10,120);
        rndG = random(80,120);
        rndB = random(130,250);
        colr = color(rndR, rndG, rndB);
    }else{
        rndR = random(130,250);
        rndG = random(80,120);
        rndB = random(10,20);
        colr = color(rndR, rndG, rndB);
    }
    masa = random(100,200);
  }
  public void draw() {
    fill(colr);
    noStroke();
    ellipse(posicion.x, posicion.y, 10, 10);
  }
  public void update() {
    velocidad.add(aceleracion);
    posicion.add(velocidad);
    aceleracion.mult(0);
  }
  public void applyForce(PVector fuerza) {
    PVector f = PVector.div(fuerza, masa);
    aceleracion.add(f);
  }
}
class Particle implements IAgent {
  PVector pos;
  PVector speed;
  PVector acc;
  float mass;
  int c;
  boolean fixed;
  float friction;
  float lifeSpan;
  float decay = 0.5f;

  public Particle(float x, float y, float mass, float friction, int colr, float lifeSpan) {
    this.pos = new PVector(x, y);
    this.speed = new PVector(0, 0);
    this.acc = new PVector(0, 0);
    this.c = colr;
    this.mass = mass;
    this.fixed = false;
    this.friction = friction;
    this.lifeSpan = lifeSpan;
  }

  public void fix() {
    fixed = true;
  }

  public void unfix() {
    fixed = false;
  }

  public void calculateFriction() {
    PVector f = speed.copy();
    f.normalize();
    f.mult(-friction);
    applyForce(f);
  }

  public void update() {
    if (!this.fixed) {
      calculateFriction();
      speed.add(acc);
      pos.add(speed);
      acc.mult(0);
    }
    lifeSpan -= decay;

    for (Bola p : pullers) {
      PVector r = PVector.sub(p.posicion, this.pos);
      float d = r.magSq();
      d = constrain(d, 1, 200);
      r.normalize();
      int dir = 1;
      if(p.pull) dir = -1;
      r.mult(dir * G * p.masa * this.mass);
      r.div(d);
      this.applyForce(r);
    }
  }

  public void draw() {
    noStroke();
    fill(c, this.lifeSpan);
    ellipse(pos.x, pos.y, mass*2, mass * 2);
  }

  public void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    speed.add(f);
  }

  public boolean isDead() {
    return this.lifeSpan <= 0;
  }
}


class ParticleSystem {
    PVector origin;
    ArrayList<PairedParticle> particles;

    ParticleSystem(float x, float y){
        origin = new PVector(x, y);
        particles = new ArrayList();
    }

    public void update(){
        Iterator<PairedParticle> i = particles.iterator();
        while(i.hasNext()){
            PairedParticle p = i.next();
            p.update();

            if(p.isDead()){
                i.remove();
            }

        }
    }

    public void draw(){
        for (PairedParticle p : particles){
            p.draw();
        }
    }

    public void applyForce(PVector force){
        for (PairedParticle p : particles){
            p.applyForce(force);
        }
    }

    public void addParticle(){
        PairedParticle p;
        float ran = random(4);
        float noi = noise(frameCount);
        p = new PairedParticle(origin.x, origin.y, particleMass, friction, restLen*5, k, restLen,  color(map(noi,0,1,40,200),map(noi,0,1,100,200),0));

        PVector dir = PVector.random2D();
        dir.setMag(random(2, 5));
        p.applyForce(dir);
        particles.add(p);
    }

}
class Spring implements IAgent{

    Particle p1;
    Particle p2;
    float k, restLen;
    int c;

    Spring(Particle p1, Particle p2, float k, float restLen){
        this.p1 = p1;
        this.p2 = p2;
        this.k = k;
        this.restLen = restLen;
        this.c = color(0,255,255);
    }

    public void update(){
        PVector springForce = PVector.sub(p2.pos, p1.pos);
        float len = springForce.mag();
        springForce.normalize();
        float displacement = len - restLen;
        springForce.mult(-k * displacement);
        springForce.div(2);
        p2.applyForce(springForce);
        springForce.mult(-1);
        p1.applyForce(springForce);
    }

    public void draw(){
        stroke(c);
        line(p1.pos.x,p1.pos.y,p2.pos.x,p2.pos.y);
    }
}
class PairedParticle implements IAgent {

    Particle partner;
    Particle hero;
    Spring joint;
    float k;
    float restLen;
    ArrayList<IAgent> agents;

    public PairedParticle(float x, float y, float mass, float friction,
        float length, float k, float restLen, int colr) {
        agents = new ArrayList();
        mass = random(1,mass);
        hero = new Particle(x,y,mass,friction,colr,lifeSpan);
        partner = new Particle(x+length,y,mass*0.7f,friction,color(colr,150),lifeSpan);
        joint = new Spring(partner, hero, k, restLen);
        agents.add(hero);
        agents.add(partner);
        agents.add(joint);
    }

    public void draw(){
        partner.draw();
        joint.draw();
        hero.draw();
    }

    public void update(){
        partner.update();
        joint.update();
        hero.update();
    }

    public void applyForce(PVector force){
        hero.applyForce(force);
        partner.applyForce(force);
    }

    public boolean isDead(){
      return hero.lifeSpan <= 0 & partner.lifeSpan <= 0;
    }
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tarea6_MarcelloAvila" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
