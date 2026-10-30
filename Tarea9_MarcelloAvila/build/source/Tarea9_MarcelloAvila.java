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

public class Tarea9_MarcelloAvila extends PApplet {

/*
 Simulaci\u00f3n de Sistemas Naturales
 Marcello \u00c1vila
 2014084118

 Tarea 9 - Guided flocking

 Descripci\u00f3n:
    Se crea un camino con el mouse, al presionar ENTER se cierra el ciclo y con el
      click se crear\u00e1n veh\u00edculos que sigan la ruta trazada pero tengan comportamiento
      de flocking, con una mayor prioridad en la caracter\u00edstica de separaci\u00f3n.
    Con R se reinician los veh\u00edculos.
    Con Q se reinicia el camino.
    Con G se guardan los recuadros.
    Con P se pausa el movimiento de los veh\u00edculos
*/

ArrayList<Vehicle> vehicles;
Path p;
boolean settingPoints = true;
boolean pause = false;
boolean save = false;

public void setup() {
  //size(800, 600);
  
  vehicles = new ArrayList();
  p = new Path();
  background(0);
}

public void draw() {
  if (!pause) {
    background(0);
    p.display();
    for (Vehicle v : vehicles) {
      v.flock(vehicles);
      v.follow(p);
      v.update();
      v.borders();
      v.display();
    }
  }
  if (mousePressed) {
    if (settingPoints) {
      p.addPoint(mouseX, mouseY);
    } else {
      float maxSpeed = 3;
      Vehicle v = new Vehicle(mouseX, mouseY, PVector.random2D().mult(maxSpeed), maxSpeed, 0.5f);
      vehicles.add(v);
    }
  }
  if (save) {
    saveFrame("img\\####.png");
  }
}
public void keyPressed() {
  if (key == '\n') {
    settingPoints = false;
  }
  if ((key == 'p') || (key == 'P')) {
    pause = !pause;
  }
  if ((key == 'g') || (key == 'G')) {
    save = !save;
  }
  if ((key == 'R') || (key == 'r')) {
    vehicles.clear();
  }
  if ((key == 'q') || (key == 'Q')) {
    p = new Path();
    settingPoints = true;
  }
}
class Path {
  ArrayList<PVector> points;
  float radius;
  int col = color(0,255,255);

  Path() {
    radius = 30;
    points = new ArrayList();
  }
  public void addPoint(float x, float y) {
    PVector point = new PVector(x, y);
    points.add(point);
  }
  public void display() {
    for (int i = 0; i < points.size() - 1; i++) {
      PVector start = points.get(i);
      PVector end = points.get(i + 1);
      strokeWeight(radius * 2);
      stroke(col,2);
      line(start.x, start.y, end.x, end.y);
    }
    for (int i = 0; i < points.size() - 1; i++) {
      PVector start = points.get(i);
      PVector end = points.get(i + 1);
      strokeWeight(1);
      stroke(col);
      line(start.x, start.y, end.x, end.y);
    }
  }
  public ArrayList<Segment> getSegments() {
    ArrayList<Segment> segments = new ArrayList();
    for (int i = 0; i < points.size() - 1; i++) {
      PVector start = points.get(i);
      PVector end = points.get(i + 1);
      Segment s = new Segment(start, end);
      segments.add(s);
    }
    return segments;
  }
}
class Segment {
  PVector start;
  PVector end;
  Segment(PVector start, PVector end) {
    this.start = start;
    this.end = end;
  }
}
class Vehicle {
  PVector pos;
  PVector vel;
  PVector acc;
  float mass = 1;
  float r = 3;
  float maxSpeed;
  float maxForce;
  float alignmentDistance;
  float alignmentRatio;
  float separationDistance;
  float separationRatio;
  float cohesionDistance;
  float cohesionRatio;


  float maxPathDistance = 200;
  float lookAhead = 50;
  float pathLookAhead = 20;
  int c;
  boolean debug = false;

  Vehicle(float x, float y, PVector vel, float maxSpeed, float maxForce) {
    pos = new PVector(x, y);
    this.vel = vel;
    acc = new PVector(0, 0);
    this.maxSpeed = maxSpeed;
    this.maxForce = maxForce;
    separationDistance = 50;
    separationRatio = 3;
    alignmentDistance = 70;
    alignmentRatio = 1;
    cohesionDistance = 70;
    cohesionRatio = 1;
    c = color(random(100, 255), 58, random(0, 255));
  }
  public void update() {
    vel.add(acc);
    vel.limit(maxSpeed);
    pos.add(vel);
    acc.mult(0);
  }
  public void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    acc.add(f);
  }
  public void seek(PVector target) {
    PVector desired = PVector.sub(target, pos);
    desired.setMag(maxSpeed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);
  }
  public void display() {
    float ang = vel.heading();
    noStroke();
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    beginShape();
    vertex(r * 3, 0);
    vertex(0, -r);
    vertex(0, r);
    endShape(CLOSE);
    if (debug) {
      noFill();
      strokeWeight(1);
      stroke(255, 0, 0, 100);
      ellipse(0, 0, cohesionDistance * 2, cohesionDistance * 2);
      stroke(0, 255, 0, 100);
      ellipse(0, 0, alignmentDistance * 2, alignmentDistance * 2);
      stroke(128, 128, 255, 100);
      ellipse(0, 0, separationDistance * 2, separationDistance * 2);
    }
    popMatrix();
  }
  public void borders() {
    pos.x = (pos.x + width) % width;
    pos.y = (pos.y + height) % height;
  }
  public void align(ArrayList<Vehicle> vehicles) {
    PVector average = new PVector(0, 0);
    int count = 0;
    for (Vehicle v : vehicles) {
      float d = PVector.dist(pos, v.pos);
      if (this != v && d < alignmentDistance) {
        average.add(v.vel);
        count++;
      }
    }
    if (count > 0) {
      average.div(count);
      average.setMag(alignmentRatio);
      average.limit(maxForce);
      applyForce(average);
    }
  }
  public void separate(ArrayList<Vehicle> vehicles) {
    PVector average = new PVector(0, 0);
    int count = 0;
    for (Vehicle v : vehicles) {
      float d = PVector.dist(pos, v.pos);
      if (this != v && d < separationDistance) {
        PVector difference = PVector.sub(pos, v.pos);
        difference.normalize();
        difference.div(d);
        average.add(difference);
        count++;
      }
    }
    if (count > 0) {
      average.div(count);
      average.setMag(separationRatio);
      average.limit(maxForce);
      applyForce(average);
    }
  }
  public void cohere(ArrayList<Vehicle> vehicles) {
    PVector center = new PVector(0, 0);
    int count = 0;
    for (Vehicle v : vehicles) {
      float d = PVector.dist(pos, v.pos);
      if (this != v && d < cohesionDistance) {
        center.add(v.pos);
        count++;
      }
    }
    if (count > 0) {
      center.div(count);
      PVector force = center.sub(pos);
      force.setMag(cohesionRatio);
      force.limit(maxForce);
      applyForce(force);
    }
  }
  public void flock(ArrayList<Vehicle> vehicles) {
    separate(vehicles);
    align(vehicles);
    cohere(vehicles);
  }
  public void follow(Path path) {
    PVector predicted = getPredictedPos();
    PVector normal = getClosestNormalPoint(path, predicted);
    float distance = PVector.dist(predicted, normal);
    if (distance > path.radius) {
      seek(normal);
    }
    if (debug) {
      noStroke();
      fill(0, 0, 255);
      ellipse(predicted.x, predicted.y, 5, 5);
      fill(255, 0, 0);
      ellipse(normal.x, normal.y, 5, 5);
    }
  }
  public PVector getPredictedPos() {
    PVector predicted = vel.copy();
    predicted.setMag(lookAhead);
    predicted.add(pos);
    return predicted;
  }
  public PVector getClosestNormalPoint(Path path, PVector predicted) {
    ArrayList<PVector> normalPoints = getNormalPoints(path, predicted);
    PVector closest = getClosest(normalPoints, predicted);
    return closest;
  }
  public ArrayList<PVector> getNormalPoints(Path path, PVector predicted) {
    ArrayList<PVector> normalPoints = new ArrayList();
    for (Segment s : path.getSegments()) {
      PVector start, end;
      if (s.start.x < s.end.x) {
        start = s.start;
        end = s.end;
      } else {
        start = s.end;
        end = s.start;
      }
      PVector a = PVector.sub(predicted,start);
      PVector b = PVector.sub(end, start);
      b.normalize();
      b.mult(a.dot(b) + pathLookAhead);
      b.add(start);
      if ((b.x >= start.x && b.x <= end.x) || (b.x >= end.x && b.x <= start.x)) {
        if ((b.y >= start.y && b.y <= end.y) || (b.y >= end.y && b.y <= start.y)) {
          normalPoints.add(b);
        }
      }
    }
    return normalPoints;
  }
  // si no hay puntos normales v\u00e1lidos, retorna la predicci\u00f3n
  public PVector getClosest(ArrayList<PVector> normalPoints, PVector predicted) {
    if (normalPoints.size() == 0) {
      return predicted;
    }
    PVector closest = normalPoints.get(0); // cambiar esto para que utilice un punto muy lejano
    for (int i = 1; i < normalPoints.size(); i++) {
      if (predicted.dist(normalPoints.get(i)) < predicted.dist(closest)) {
        closest = normalPoints.get(i);
      }
    }
    if (predicted.dist(closest) < maxPathDistance) {
      return closest;
    } else {
      return predicted;
    }
  }
}
class VehicleOld {
  PVector pos;
  PVector vel;
  PVector acc;
  float mass = 1;
  float r = 5;
  float maxSpeed;
  float maxForce;
  float maxPathDistance = 200;
  float lookAhead = 50;
  float pathLookAhead = 20;
  int c;
  boolean debug = false;

  VehicleOld(float x, float y, PVector vel, float maxSpeed, float maxForce) {
    pos = new PVector(x, y);
    this.vel = vel;
    acc = new PVector(0, 0);
    this.maxSpeed = maxSpeed;
    this.maxForce = maxForce;
    c = color(random(100, 255), 58, random(0, 255));
  }
  public void update() {
    vel.add(acc);
    vel.limit(maxSpeed);
    pos.add(vel);
    acc.mult(0);
  }
  public void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    acc.add(f);
  }
  public void seek(PVector target) {
    PVector desired = PVector.sub(target, pos);
    desired.setMag(maxSpeed);
    PVector steering = PVector.sub(desired, vel);
    steering.limit(maxForce);
    applyForce(steering);
  }
  public void display() {
    float ang = vel.heading();
    stroke(255);
    strokeWeight(1);
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    beginShape();
    vertex(r * 3, 0);
    vertex(0, -r);
    vertex(0, r);
    endShape(CLOSE);
    popMatrix();
  }
  public void borders() {
    pos.x = (pos.x + width) % width;
    pos.y = (pos.y + height) % height;
  }
  public void follow(Path path) {
    PVector predicted = getPredictedPos();
    PVector normal = getClosestNormalPoint(path, predicted);
    float distance = PVector.dist(predicted, normal);
    if (distance > path.radius) {
      seek(normal);
    }
    if (debug) {
      noStroke();
      fill(0, 0, 255);
      ellipse(predicted.x, predicted.y, 5, 5);
      fill(255, 0, 0);
      ellipse(normal.x, normal.y, 5, 5);
    }
  }
  public PVector getPredictedPos() {
    PVector predicted = vel.copy();
    predicted.setMag(lookAhead);
    predicted.add(pos);
    return predicted;
  }
  public PVector getClosestNormalPoint(Path path, PVector predicted) {
    ArrayList<PVector> normalPoints = getNormalPoints(path, predicted);
    PVector closest = getClosest(normalPoints, predicted);
    return closest;
  }
  public ArrayList<PVector> getNormalPoints(Path path, PVector predicted) {
    ArrayList<PVector> normalPoints = new ArrayList();
    for (Segment s : path.getSegments()) {
      PVector start, end;
      if (s.start.x < s.end.x) {
        start = s.start;
        end = s.end;
      } else {
        start = s.end;
        end = s.start;
      }
      PVector a = PVector.sub(predicted,start);
      PVector b = PVector.sub(end, start);
      b.normalize();
      b.mult(a.dot(b) + pathLookAhead);
      b.add(start);
      if ((b.x >= start.x && b.x <= end.x) || (b.x >= end.x && b.x <= start.x)) {
        if ((b.y >= start.y && b.y <= end.y) || (b.y >= end.y && b.y <= start.y)) {
          normalPoints.add(b);
        }
      }
    }
    return normalPoints;
  }
  // si no hay puntos normales v\u00e1lidos, retorna la predicci\u00f3n
  public PVector getClosest(ArrayList<PVector> normalPoints, PVector predicted) {
    if (normalPoints.size() == 0) {
      return predicted;
    }
    PVector closest = normalPoints.get(0); // cambiar esto para que utilice un punto muy lejano
    for (int i = 1; i < normalPoints.size(); i++) {
      if (predicted.dist(normalPoints.get(i)) < predicted.dist(closest)) {
        closest = normalPoints.get(i);
      }
    }
    if (predicted.dist(closest) < maxPathDistance) {
      return closest;
    } else {
      return predicted;
    }
  }
}
  public void settings() {  fullScreen(FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tarea9_MarcelloAvila" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
