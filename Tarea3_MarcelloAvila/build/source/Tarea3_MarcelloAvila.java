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

public class Tarea3_MarcelloAvila extends PApplet {

/*
 Simulaci\u00f3n de Sistemas Naturales
 Marcello \u00c1vila
 2014084118

 Tarea 3 - Estrellas (?)

 Descripci\u00f3n:
   Se crean 600 estrellas y todas ejercen una fuerza repelente sobre las dem\u00e1s.
   Cada una tiene una masa variable entre 10 y 40.
   Existe un sol, guiado por el mouse, con una masa de 10.000, que ejerce
     una fuerza de atracci\u00f3n por las estrellas.
   Al hacer click, el sol desprende una onda expansiva donde repele a todas
     las estrellas dentro de un rango de alcance variable.
 */
Bola[] bolas;
PImage bg;

int stars = 600;
int sunSize = 40;
int sunMass = 10000;
float wave = 2.6f;
float G = 0.0005f;
PVector mouse;
PVector pull;

public void setup() {
  
  bolas = new Bola[stars];
  for (int i = 0; i < bolas.length; i++) {
    bolas[i] = new Bola(random(0, width), random(0, height));
  }
  background(0);
  bg = loadImage("space.png");
}

public void draw() {
  fill(0,0,0,30);
  noStroke();
  rect(0,0,width,height);
  fill(155,155,0,150);
  ellipse(mouseX,mouseY,sunSize,sunSize);
  for (Bola b1 : bolas) {
    mouse = new PVector(mouseX, mouseY);
    pull = PVector.sub(mouse,b1.posicion);
    float moused = pull.magSq();
    moused = constrain(moused, 1, 200);
    pull.normalize();

    int w = PApplet.parseInt(sunSize * wave);
    if(mousePressed & (abs(b1.posicion.x-mouse.x) < w/2) & (abs(b1.posicion.y-mouse.y) < w/2)){
      noFill();
      for(int i = w/2; i < w; i+=4){
        stroke(255,0,0,map(i,w,w/2,5,25));
        ellipse(mouse.x, mouse.y,i,i);
      }
      pull.mult(-1 * G * b1.masa * sunMass);
    }{
      noStroke();
      pull.mult(G * b1.masa * sunMass);
    }

    pull.div(moused);
    b1.aplicarFuerza(pull);
    for (Bola b2 : bolas) {
      if (b1 != b2) {
        PVector r = PVector.sub(b2.posicion, b1.posicion);
        float d = r.magSq();
        d = constrain(d, 1, 200);
        r.normalize();
        r.mult(-1 * G * b1.masa * b2.masa);
        r.div(d);
        b1.aplicarFuerza(r);
      }
    }
    b1.actualizar();
    b1.dibujar();
  }
}
public class Bola {
  PVector posicion;
  PVector velocidad;
  PVector aceleracion;
  float masa, rndR, rndG, rndB;
  Bola(float x, float y) {
    posicion = new PVector(x, y);
    velocidad = new PVector(0, 0);
    aceleracion = new PVector(0, 0);
    masa = random(10, 25);
    rndR = random(10,120);
    rndG = random(80,120);
    rndB = random(130,250);
  }
  public void dibujar() {
    fill(rndR, rndG, rndB);
    noStroke();
    ellipse(posicion.x, posicion.y, masa, masa);
  }
  public void actualizar() {
    velocidad.add(aceleracion);
    posicion.add(velocidad);
    aceleracion.mult(0);
  }
  public void aplicarFuerza(PVector fuerza) {
    PVector f = PVector.div(fuerza, masa);
    aceleracion.add(f);
  }
}
  public void settings() {  size(800, 600, FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tarea3_MarcelloAvila" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
