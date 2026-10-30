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

public class ejemploRotaciones extends PApplet {

Bola[] bolas;

public void setup() {
  
  bolas = new Bola[10];
  for (int i = 0; i < bolas.length; i++) {
    bolas[i] = new Cuadro(random(width), random(height));
  }
  background(0);
}

public void draw() {
  for (Bola b1 : bolas) {
    for (Bola b2 : bolas) {
      if (b1 != b2) {
        float G = 1;
        PVector r = PVector.sub(b2.posicion, b1.posicion);
        float d = r.magSq();
        d = constrain(d, 1, 200);
        r.normalize();
        r.mult(G * b1.masa * b2.masa);
        r.div(d);
        b1.aplicarFuerza(r);
      }
    }
    b1.actualizar();
    b1.bordes();
    b1.dibujar();
  }
}
public class Bola {
  PVector posicion;
  PVector velocidad;
  PVector aceleracion;
  float masa;
  int col;
  Bola(float x, float y) {
    posicion = new PVector(x, y);
    velocidad = new PVector(0, 0);
    aceleracion = new PVector(0, 0);
    masa = randomGaussian()*5 + 20;
    col = color(0,0,random(100,255),40);
  }
  public void dibujar() {
    noStroke();
    fill(col);
    ellipse(posicion.x, posicion.y, masa * 20, masa * 20);
  }
  public void bordes() {
    if (posicion.x < 0 || posicion.x > width) {
      velocidad.x *= -1;
      posicion.x = constrain(posicion.x, 0, width);
    }
    if (posicion.y < 0 || posicion.y > height) {
      velocidad.y *= -1;
      posicion.y = constrain(posicion.y, 0, height);
    }
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
class Cuadro extends Bola {
  float ang;
  float velAng;
  float accAng;
int col;
  Cuadro(float x, float y){
    super(x, y);
    ang = random(2*PI);
    velAng = 0;
    accAng = 0;
    col = color(random(0,255),0,random(100,255),20);
  }

  public void dibujar(){
    ang += velAng;
    velAng += accAng;
    accAng = map(velocidad.mag()/3, 0, 46, -0.001f, 0.001f);
    pushMatrix();
    translate(posicion.x, posicion.y);
    rotate(ang);
    fill(col);
    rectMode(CENTER);
    noStroke();
    rect(0, 0, masa, masa);
    popMatrix();
  }

}
  public void settings() {  size(800, 600,FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ejemploRotaciones" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
