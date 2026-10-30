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

public class FlowFieldFollowing extends PApplet {

FlowField field;
ArrayList<Vehicle> vehicles;
float defaultMag;

public void setup(){
    
    defaultMag = 1;
    field = new FlowField(25,defaultMag);
    vehicles = new ArrayList();
    background(0);
}
public void draw(){
    //fill(0,30);
    //noStroke();
    //rect(0,0,width,height);

    background(0);
    field.display();
    field.update();
    PVector mouse = new PVector(mouseX, mouseY);
    stroke(255);
    fill(255,10);
    ellipse(mouse.x,mouse.y,200,200);
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
        float r = map(noise(frameCount*3),0,1,200,255);
        float g = map(noise(frameCount*2),0,1,150,255);
        float b = map(noise(frameCount*0.9f),0,1,200,255);
        vehicles.add(new Vehicle(mouseX, mouseY, PVector.random2D(), color(r,g,b)));
    }
    if(keyPressed && (key == 'r' || key == 'R')) vehicles.clear();
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
                float angle = map(noise(c*0.5f, r*0.1f, frameCount*0.005f),0,1,0,TWO_PI);
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
        if(x >= 0 && x <= width){
            if(y >= 0 && y <= height){
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
    mass = random(1.5f,3);
    size = map(mass,1.5f,3,5,15);
    maxSpeed = map(mass,1.5f,3,7,2);
    maxForce = 10;
    c = colour;
    arrivalRadius = 200;
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
    String[] appletArgs = new String[] { "FlowFieldFollowing" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
