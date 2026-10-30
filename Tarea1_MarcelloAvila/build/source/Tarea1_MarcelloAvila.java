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

public class Tarea1_MarcelloAvila extends PApplet {

/*
 Simulaci\u00f3n de Sistemas Naturales
 Marcello \u00c1vila
 2014084118

 Tarea 1 - Globos de Helio

 Descripci\u00f3n:
   Cada globo inicia con direcci\u00f3n aleatoria y magnitud.
   Existe una rafaga de viento constante hacia la izquierda y al
     hacer click con el mouse, esta cambia de direcci\u00f3n y aumenta
     su rango de intensidad posible.
   Al hacer click sobre un globo, este se estalla y cae al suelo.
   Hay una sorpresa al estallar todos los globos.
 */

ArrayList<Ball> bolas = new ArrayList<Ball>();
float time = 0;
float windStr;
int NUMBALLS = 50;
int poppedBalls = 0;
boolean ended = false;

public void setup() {
  
  for (int i = 0; i < NUMBALLS; i++) {
    float posX = map(random(100), 0, 100, 250, width-250);
    float posY = map(random(100), 0, 100, 350, height-50);
    bolas.add(new Ball(posX, posY, PVector.random2D()));
  }
}

public void draw() {
  if (!ended) {
    background(10, 120, 180);
    PVector viento;
    for (Ball b : bolas) {
      windStr = noise(b.pos.x, b.pos.y, time);
      windStr = map(windStr, 0, 1, 0, 0.03f);
      viento = new PVector(windStr, 0);
      if (mousePressed) {
        // Viento izquierda
        windStr = map(windStr, 0, 1, 0.1f, 0.2f);
        viento = new PVector(windStr, 0);
        viento.setMag(viento.mag() * -1);
      }
      if (b.standing) {
        poppedBalls++;
      }
      b.aplicarFuerza(viento);
      b.draw();
      b.refresh();
    }
    if (poppedBalls == NUMBALLS) {
      ended = true;
    }
    time += 0.01f;
    poppedBalls = 0;
  } else {
    textSize(50);
    fill(0, 102, 153);
    text("Por qu\u00e9 sos as\u00ed? :(", width*0.1f, height*0.2f);
  }
}
class Ball {
  int BALLSIZE = 40;
  float BOUNCE = 2.28f;
  float HELIUMPRESSURE = -0.035f;

  float REDNOISE = 4;
  float GREENNOISE = 2;
  float BLUENOISE = 3;

  PVector pos;
  PVector speed;
  PVector acc;
  float rndR, rndG, rndB;
  int rgb;
  boolean popped = false;
  boolean standing = false;
  PVector gravity;

  Ball(float x, float y, PVector v) {
    pos = new PVector (x, y);
    speed = new PVector (v.x, v.y);
    acc = PVector.random2D();

    rgb = color(random(200), random(200), random(200), map(random(5), 0, 5, 100, 255));
  }

  public void refresh() {
    if (!standing) {
      gravity = new PVector(0, 0.01f);
      aplicarFuerza(gravity);
      if (!popped) {
        PVector helio = new PVector(0, HELIUMPRESSURE);
        aplicarFuerza(helio);
      }
      speed.add(acc);
      speed.limit(5);
      pos.add(speed);
      acc.mult(0);
      checkBorders();
      checkClick();
    }
  }

  public void draw() {
    noStroke();
    fill(rgb);
    ellipse(pos.x, pos.y, BALLSIZE, BALLSIZE);
    if (popped) {
      stroke(255,0,0);
      line(pos.x-BALLSIZE/2,pos.y-BALLSIZE/2,pos.x+BALLSIZE/2,pos.y+BALLSIZE/2);
      line(pos.x-BALLSIZE/2,pos.y+BALLSIZE/2,pos.x+BALLSIZE/2,pos.y-BALLSIZE/2);
    }
  }

  public void aplicarFuerza(PVector fuerza) {
    acc.add(fuerza);
  }

  public void checkBorders() {
    if (pos.x + BALLSIZE/2 > width) {
      speed = speed.mult(0.8f);
      aplicarFuerza(new PVector(-speed.x * BOUNCE, 0));
    }
    if (pos.x - BALLSIZE/2 < 0) {
      speed = speed.mult(0.8f);
      aplicarFuerza(new PVector(-speed.x * BOUNCE, 0));
    }
    if (!popped) {
      if (pos.y + BALLSIZE/2 > height) {
        speed = speed.mult(0.8f);
        aplicarFuerza(new PVector(0, -speed.y * BOUNCE));
      }
      if (pos.y - BALLSIZE/2 < 0) {
        speed = speed.mult(0.8f);
        aplicarFuerza(new PVector(0, -speed.y * BOUNCE));
      }
    } else {
      if (pos.y + BALLSIZE/2 > height) {
        acc.mult(0);
        speed = speed.mult(0);
        standing = true;
      }
    }
  }

  public void checkClick() {
    // Revisa si el mouse clicke\u00f3 el globo
    if (mousePressed) {
      if (
        (abs(mouseX-pos.x) < BALLSIZE/2) && 
        (abs(mouseY-pos.y) < BALLSIZE/2))
      {
        popped = true;
        aplicarFuerza(new PVector(0, 1));
      }
    }
  }
}
  public void settings() {  size(800, 600, FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tarea1_MarcelloAvila" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
