import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class resortes extends PApplet {



ControlP5 cp5;

Particle p1, p2;
Spring s;
ArrayList<IAgent> agents;
float margin = 20;
float n = 4;
float mass = 2;
float friction = 0.1f;
float k = 0.2f;
float restLen = 0;
PVector gravity;
float g = 2;

public void setup(){
    
    background(100,200,100);/*
    p1 = new Particle(width/2-100, height/2, 5, 0.1);
    p2 = new Particle(width/2+100, height/2, 5, 0.1);
    s = new Spring(p1,p2,0.1,150.0);*/
    Particle old = null;
    agents = new ArrayList();
    float sep = (width - 2 * n) / (n-1);
    ArrayList<Particle> particles = new ArrayList();
    for(int i = 0; i < n; i++){
        Particle p = new Particle(i*sep + margin, 100, mass, friction);
        if(i == 0 || i == n-1) p.fix();
        agents.add(p);
        if(i > 0){
            Spring s = new Spring(p, old, k, restLen);
            agents.add(s);
        }
        old = p;
    }
    gravity = new PVector(0, g);
    initControl();
}

public void draw(){
    background(0);
    for(IAgent a : agents){
        if(a instanceof Particle){
            Particle p = (Particle)a;
            p.applyForce(gravity);
        }
        a.update();
        a.draw();
    }
}

public void setRestLen(float value){
    for(IAgent a : agents){
        if(a instanceof Spring){
            Spring s = (Spring)a;
            s.restLen = value;
        }
    }
}

public void initControl(){
    cp5 = new ControlP5(this);
    cp5.addSlider("setRestLen")
        .setPosition(10,10)
        .setSize(300,20)
        .setRange(0,50)
        .setValue(restLen)
        .setCaptionLabel("Rest Length");
}
class Particle implements IAgent{
    PVector pos, speed, accel;
    float mass, friction;
    int c;
    boolean fixed;

    Particle(float x, float y, float mass, float friction){
        pos = new PVector(x,y);
        speed = new PVector(0,0);
        accel = new PVector(0,0);
        this.mass = mass;
        this.friction = friction;
        c = color(0,128,128,100);
        fixed = false;
    }

    public void update(){
        if(!this.fixed){
            calculateFriction();
            pos.add(speed);
            speed.add(accel);
            accel.mult(0);
        }
    }

    public void draw(){
        noStroke();
        fill(c);
        ellipse(pos.x,pos.y,mass*5,mass*5);
    }

    public void applyForce(PVector force){
        PVector f = PVector.div(force,mass);
        speed.add(f);
    }

    public void fix(){
        this.fixed = true;
    }

    public void unfix(){
        this.fixed = false;
    }

    public void calculateFriction(){
        PVector f = speed.copy();
        f.normalize();
        f.mult(-friction);
        applyForce(f);
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
        this.c = color(255);
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
  public void settings() {  size(800,600, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "resortes" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
