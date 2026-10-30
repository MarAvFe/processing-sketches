import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Tarea8_MarcelloAvila extends PApplet {

/*
 Simulaci\u00f3n de Sistemas Naturales
 Marcello \u00c1vila
 2014084118

 Tarea 7 - Mouse Seeking

 Descripci\u00f3n:
    Se crea un flow field con ruido perlin.
    Con el click del mouse se crean veh\u00edculos con direcci\u00f3n aleatoria
        en la posici\u00f3n del mouse. Su color tambi\u00e9n ser\u00e1 aleatorio,
        basado en ruido perlin.
    Cada veh\u00edculo cuenta con una masa de entre [1-5] aleatoriamente. Seg\u00fan
        el valor obtenido se mapean su tama\u00f1o de [5-15], su velocidad
        m\u00e1xima de [5-2] de tal manera que los m\u00e1s grandes sean m\u00e1s lentos.
        Y su fuerza m\u00e1xima de [0.04-0.1] como el ejemplo planteaba. Adem\u00e1s
        su radio de llegada ser\u00e1 de [50,200].
    La zona blanca alrededor del mouse, dibuja el radio m\u00e1ximo de llegada
        de 200px.
    Se puede dibujar el rastro de los veh\u00edculos, pero es una caracter\u00edstica
        pesada. Se recomienda activarla solo con el flow field apagado y pocos
        veh\u00edculos en pantalla.

Acciones:
    s : (s)how flow field
    m : show (m)ouse radius
    r : (r)eset vehicles to zero
    t : (t)rail on/off
*/

FlowField field;
ArrayList<Vehicle> vehicles;
float defaultMag;
boolean showField;
boolean showMouse;
boolean showTrail;
int RES = 25;

public void setup(){
    
    defaultMag = 1;
    field = new FlowField(RES,defaultMag);
    vehicles = new ArrayList();
    showField = true;
    showMouse = true;
    showTrail = false;
    background(0);
}
public void draw(){
    if(showTrail){
        fill(0,30);
        noStroke();
        rect(0,0,width,height);
    }else{
        background(0);
    }
    if(showField) field.display();
    field.update();
    PVector mouse = new PVector(mouseX, mouseY);
    stroke(255);
    fill(255,10);
    if(showMouse) ellipse(mouse.x,mouse.y,200,200);
    for(Vehicle v : vehicles){
        PVector mouseSeek = PVector.sub(mouse,v.pos);
        mouseSeek.setMag(defaultMag);
        v.applyForce(mouseSeek);
        PVector f = field.getForce(v.pos.x, v.pos.y);
        v.applyForce(f);
        v.update();
        v.display();
    }
    if(mousePressed){
        float r = map(noise(frameCount*0.01f,1),0,1,0,255);
        float g = map(noise(frameCount*0.01f,4),0,1,0,255);
        float b = map(noise(frameCount*0.01f,7),0,1,0,255);
        colorMode(HSB);
        vehicles.add(new Vehicle(mouseX, mouseY, PVector.random2D(), color(r,g,b,80)));
    }
    if(keyPressed && (key == 'r' || key == 'R')) vehicles.clear();
}

public void keyPressed(){
    if(key == 's' || key == 'S') showField = !showField;
    if(key == 'm' || key == 'M') showMouse = !showMouse;
    if(key == 't' || key == 'T') showTrail = !showTrail;
}
class FlowField{
    PVector[][] grid;
    float resolution;
    int rows;
    int columns;
    float defaultMag;

    FlowField(float resolution, float defaultMag){
        this.resolution = resolution;
        this.defaultMag = defaultMag;
        rows = (int)(height / resolution);
        columns = (int)(width / resolution);
        initGrid();
        update();
    }
    public void initGrid(){
        grid = new PVector[rows][];
        for (int r = 0; r < rows; r++){
            grid[r] = new PVector[columns];
        }
    }
    public void update(){
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < columns; c++){
                float angle = map(noise(c*0.1f, r*0.1f, frameCount*0.0025f),0,1,-TWO_PI,TWO_PI);
                grid[r][c] = PVector.fromAngle(angle);
                grid[r][c].setMag(defaultMag);
            }
        }
    }
    public void display(){
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < columns; c++){
                displayVector(grid[r][c], c * resolution, r * resolution);
            }
        }
    }
    public void displayVector(PVector vector, float x, float y){
        PVector v = vector.copy();
        v.setMag(resolution/2);
        pushMatrix();
        translate(x + resolution / 2, y + resolution / 2);
        stroke(100,0,100,90);
        strokeWeight(1);
        line(0,0,v.x,v.y);
        ellipse(v.x,v.y,2,2);
        popMatrix();
    }
    public PVector getForce(float x, float y){
        if(x >= 0 && x <= width-1){
            if(y >= 0 && y <= height-1){
                int r = (int)(y/resolution);
                int c = (int)(x/resolution);
                return grid[r][c];
            }
        }
        return new PVector(0,0);
    }
}
class Vehicle {
  PVector pos;
  PVector vel;
  PVector acc;
  float mass;
  float size;
  float maxSpeed;
  float maxForce;
  int c;
  float arrivalRadius;

  Vehicle(float x, float y, PVector vel, int colour) {
    pos = new PVector(x, y);
    this.vel = vel;
    acc = new PVector(0, 0);
    float loBound = 1;
    float hiBound = 5;
    mass = random(loBound,hiBound);
    size = map(mass,loBound,hiBound,5,15);
    maxSpeed = map(mass,loBound,hiBound,5,2);
    maxForce = map(mass,loBound,hiBound,0.04f,0.1f);
    c = colour;
    arrivalRadius = map(mass,loBound,hiBound,50,200);
  }

  public void update() {
    vel.add(acc);
    vel.limit(maxSpeed);
    pos.add(vel);
    acc.mult(0);
    borders();
  }

  public void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    acc.add(f);
  }

  public void display() {
    float ang = vel.heading();
    //stroke(255);
    //strokeWeight(3);
    pushMatrix();
    pushStyle();
    noStroke();
    fill(c,200);
    translate(pos.x, pos.y);
    rotate(ang);
    beginShape();
    vertex(size * 3, 0);
    vertex(0, -size);
    vertex(0, size);
    endShape(CLOSE);
    popStyle();
    popMatrix();
  }
  public void seek(PVector target){
    PVector desired = PVector.sub(target, pos);
    desired.setMag(maxSpeed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);
  }

  public void arrive(PVector target){
    PVector desired = PVector.sub(target, pos);
    float d = PVector.dist(pos, target);
    d = constrain(d, 0, arrivalRadius);
    float speed = map(d, 0, arrivalRadius, 0, maxSpeed);
    vel.setMag(speed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);

  }
  public void borders(){
      pos.x = (pos.x + width)%width;
      pos.y = (pos.y + height)%height;
  }

}
  public void settings() {  size(800,600,FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tarea8_MarcelloAvila" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
