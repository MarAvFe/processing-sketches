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

public class Tarea5_MarcelloAvila extends PApplet {

// Marcello \u00c1vila
// Se hace viento constante desde el mouse hacia cada part\u00edcula, al hacer click.
// Por alguna raz\u00f3n se descontrolan las part\u00edculas con
//   algunas combinaciones de par\u00e1metros
// El grosor de los resortes cambia seg\u00fan su tensi\u00f3n.




ControlP5 cp5;

Particle p1, p2;
Spring s;
ArrayList<IAgent> agents;
float margin = height*0.2f;
float n = 10;
float h = 10;
float k = 4.5f;
float g = 9.8f;
float mass = 8;
float friction = 2;
PVector gravity;

float leftBorder, rightBorder, topBorder, bottomBorder, restLen;

public void setup(){
    //size(800,600, P2D);
    
    background(100,200,100);
    Particle left = null;
    agents = new ArrayList();
    float x = width  / n;
    float y = height / h;
    leftBorder = width*0.2f;
    rightBorder = width-margin;
    topBorder = height*0.2f;
    bottomBorder = height-margin;
    restLen = (rightBorder-leftBorder)/n/5;

    ArrayList<Particle> tops = new ArrayList<Particle>();
    for(int i = 0; i < n; i++) tops.add(new Particle(0,0,0,0));

    for(int j = 0; j < h; j++){
        for(int i = 0; i < n; i++){
            Particle p = new Particle(
                map(i*x,0,(n-1)*x,leftBorder,rightBorder),
                map(j*y,0,(h-1)*y,topBorder,bottomBorder),
                mass, friction);
                if(i == 0 && j == 0) p.fix();
                //if(i == 0 && j == h-1) p.fix();
                if(i == n-1 && j == 0) p.fix();
                //if(i == n-1 && j == h-1) p.fix();
                agents.add(p);

                if(i > 0){
                    Spring s = new Spring(p, left, k, restLen);
                    agents.add(s);
                }
                left = p;

                if(j > 0){
                    Spring s = new Spring(p, tops.get(i), k, restLen);
                    agents.add(s);
                }
                tops.set(i,p);
            }
            left = null;
        }
        setGravity(g);
        initControl();
    }

    public void draw(){
        background(0);

        rectMode(CORNERS);
        fill(150,0,150,100);
        rect(leftBorder,topBorder,rightBorder,bottomBorder);

        for(IAgent a : agents){
            if(a instanceof Particle){
                Particle p = (Particle)a;
                p.applyForce(gravity);
                if (mousePressed) {
                    PVector mouse = new PVector(mouseX,mouseY);
                    PVector viento = PVector.sub(mouse,p.pos);
                    viento.normalize();
                    viento.setMag(-10);
                    p.applyForce(viento);
                }
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

    public void setK(float value){
        for(IAgent a : agents){
            if(a instanceof Spring){
                Spring s = (Spring)a;
                s.k = value;
            }
        }
    }

    public void setFriction(float value){
        for(IAgent a : agents){
            if(a instanceof Particle){
                Particle p = (Particle)a;
                p.friction = value;
            }
        }
    }

    public void setMass(float value){
        for(IAgent a : agents){
            if(a instanceof Particle){
                Particle p = (Particle)a;
                p.mass = value;
            }
        }
    }

    public void setGravity(float value){
        gravity = new PVector(0, value);
    }

    public void initControl(){
        cp5 = new ControlP5(this);
        cp5.addSlider("setRestLen")
        .setPosition(10,10)
        .setSize(200,20)
        .setRange(restLen/10,restLen*2)
        .setValue(restLen)
        .setCaptionLabel("Rest Length");

        cp5.addSlider("setFriction")
        .setPosition(10,40)
        .setSize(200,20)
        .setRange(-2,5)
        .setValue(friction)
        .setCaptionLabel("Friction");

        cp5.addSlider("setK")
        .setPosition(10,70)
        .setSize(200,20)
        .setRange(1,16)
        .setValue(k)
        .setCaptionLabel("Elasticity");

        cp5.addSlider("setMass")
        .setPosition(10,100)
        .setSize(200,20)
        .setRange(2,20)
        .setValue(mass)
        .setCaptionLabel("Mass");

        cp5.addSlider("setGravity")
        .setPosition(10,130)
        .setSize(200,20)
        .setRange(-5,20)
        .setValue(g)
        .setCaptionLabel("Gravity");
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
        pushStyle();
        noStroke();
        fill(c);
        ellipse(pos.x,pos.y,mass*5,mass*5);
        popStyle();
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
        this.c = color(255,0,0,200);
    }

    public void update(){
        PVector springForce = PVector.sub(p2.pos, p1.pos);
        springForce.normalize();
        springForce.mult(-k * getDisplacement());
        springForce.div(2);
        p2.applyForce(springForce);
        springForce.mult(-1);
        p1.applyForce(springForce);


    }

    public float getDisplacement(){
        return PVector.sub(p2.pos, p1.pos).mag() - restLen;
    }

    public void draw(){
        pushStyle();
        stroke(c);
        float strength = map(getDisplacement()*k,0,restLen*10*k,0.1f,3);
          strokeWeight(strength);
        
        line(p1.pos.x,p1.pos.y,p2.pos.x,p2.pos.y);
        popStyle();
    }
}
  public void settings() {  fullScreen(P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tarea5_MarcelloAvila" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
