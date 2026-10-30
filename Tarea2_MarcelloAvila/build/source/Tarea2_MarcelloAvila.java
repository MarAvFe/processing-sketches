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

public class Tarea2_MarcelloAvila extends PApplet {

Bola[] bolas;
PVector[] fuerzas;

public void setup() {
  
  bolas = new Bola[200];
  for (int i = 0; i < bolas.length; i++) {
    bolas[i] = new Bola(random(width-100)+50, random(height-100)+50);
    bolas[i].aplicarFuerza(new PVector(randomGaussian()*4,randomGaussian()*4));
  }
}

public void draw() {
  background(0,0,0,50);
  PVector viento = new PVector(0.5f, 0);
  for (Bola b : bolas) {
    float MAXSPEED = 0.7f;
    PVector right = new PVector(map(b.posicion.x,0,width,MAXSPEED,0),0);
    PVector left = new PVector(map(b.posicion.x,width,0,-MAXSPEED,0),0);
    PVector down = new PVector(0,map(b.posicion.y,0,height,MAXSPEED,0));
    PVector up = new PVector(0,map(b.posicion.y,height,0,-MAXSPEED,0));
    if (mousePressed) {
      b.aplicarFuerza(viento);
    }
    b.aplicarFuerza(right);
    b.aplicarFuerza(left);
    b.aplicarFuerza(up);
    b.aplicarFuerza(down);
    b.actualizar();
    b.dibujar();
  }
}
public class Bola {
  PVector posicion;
  PVector velocidad;
  PVector aceleracion;
  float masa, randRed, randBlue, randGreen;
  Bola(float x, float y) {
    posicion = new PVector(x, y);
    velocidad = new PVector(0, 0);
    aceleracion = new PVector(0, 0);
    masa = random(1,3);
  }
  public void dibujar() {
    stroke(255-randRed, 255-randGreen, 255-randBlue);
    strokeWeight(0.3f);
    line(width/2, height/2, posicion.x, posicion.y);

    fill(randRed, randGreen, randBlue, 150);
    strokeWeight(masa);
    ellipse(posicion.x, posicion.y, masa * 20, masa * 20);
  }

  public void actualizar() {
    velocidad.add(aceleracion);
    posicion.add(velocidad);
    aceleracion.mult(0);
    float REDNOISE = 1;
    float BLUENOISE = 2;
    float GREENNOISE = 0.5f;
    randRed = noise(map(posicion.x, 0, width, 0, REDNOISE), map(posicion.y, 0, height, 0, REDNOISE));
    randRed = abs(map(randRed, 0, 1, 0, 255));

    randGreen = noise(map(posicion.x, 0, width, 0, GREENNOISE), map(posicion.y, 0, height, 0, GREENNOISE));
    randGreen = abs(map(randGreen, 0, 1, -255, 255));

    randBlue = noise(map(posicion.x, 0, width, 0, BLUENOISE), map(posicion.y, 0, height, 0, BLUENOISE));
    randBlue = abs(map(randBlue, 0, 1, -255, 255));
  }
  public void aplicarFuerza(PVector fuerza) {
    PVector f = PVector.div(fuerza, masa);
    aceleracion.add(f);
  }
}
  public void settings() {  size(800, 600, FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tarea2_MarcelloAvila" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
