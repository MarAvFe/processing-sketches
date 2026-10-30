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

public class Tarea0_MarcelloAvila extends PApplet {

/*
 Simulaci\u00f3n de Sistemas Naturales
 Marcello \u00c1vila
 2014084118

 Tarea 0 - Matriz de c\u00edrculos en pantalla

 Acciones:
   (ESC)ape      : Detener simulaci\u00f3n
   (P)ause       : Pausar tiempo
   (R)esume      : Continuar tiempo
   (L)oop        : Iniciar ciclo de 2 segundos en tiempo normal
   (C)ontinue    : Salir del ciclo y continuar simulaci\u00f3n
   (LEFT)        : Acelerar tiempo
   (RIGHT)       : Ralentizar tiempo
   (MOUSE)       : Invierte el color de los pixeles cercanos
   (CLICK)       : Invierte el color del fondo de pantalla

 Parametrizaci\u00f3n:
   PIXELBASE     : Define la media donde oscila el tama\u00f1o de cada pixel
   TIMESTEP      : Cantidad que se aumenta el tiempo en cada cambio de cuadro
   varios        : Todas las variables en may\u00fascula
 */

float TIMESTEP = 0.01f;
float PIXELBASE = 10;

float REDNOISE = 0.5f;
float GREENNOISE = 2;
float BLUENOISE = 5;

float SIZENOISE = 4;
float SIZEMIN = 1;
float SIZEMAX = PIXELBASE * 2.5f;

float POSNOISE = 0.8f;
float POSMIN = -50;
float POSMAX = 50;

/* ---------- Terminan par\u00e1metros ---------- */

int bgColor = 0;
float time = 0.0f;
boolean paused = false;
boolean looping = false;
boolean loopFwd = true;
float loopTop, loopBottom;

float randRed, randGreen, randBlue, randSize, randPos, posX, posY;
PVector mouse, pixel, distance;
int i, j;

public void setup() {
  
  background(0);
  //ellipseMode(RADIUS);
  noStroke();
}

public void draw() {
  background(bgColor);
  for (i = 0; i < width; i+=PIXELBASE) {
    for (j = 0; j < height; j+=PIXELBASE) {

      update();

      fill(randRed, randGreen, randBlue);
      if ((PIXELBASE*2 > abs(distance.x)) &&(PIXELBASE*2 > abs(distance.y)) ) {
        // Si el pixel est\u00e1 muy cerca del mouse, invierta el color
        fill(255-randRed, 255-randGreen, 255-randBlue);
      }

      ellipse(posX, posY, randSize, randSize);
    }
  }

  loopControl();
  checkInteractions();
}

public void update() {
  /* Actualiza valores aleatorios */
  randRed = noise(map(i, 0, width, 0, REDNOISE), map(j, 0, height, 0, REDNOISE), time);
  randRed = abs(map(randRed, 0, 1, -255, 255));

  randGreen = noise(map(i, 0, width, 0, GREENNOISE), map(j, 0, height, 0, GREENNOISE), time);
  randGreen = abs(map(randGreen, 0, 1, -255, 255));

  randBlue = noise(map(i, 0, width, 0, BLUENOISE), map(j, 0, height, 0, BLUENOISE), time);
  randBlue = abs(map(randBlue, 0, 1, -255, 255));

  randSize = noise(map(i, 0, width, 0, SIZENOISE), map(j, 0, height, 0, SIZENOISE), time);
  randSize = map(randSize, 0, 1, SIZEMIN, SIZEMAX);

  randPos = noise(map(i, 0, width, 0, POSNOISE), map(j, 0, height, 0, POSNOISE), time);
  randPos = map(randPos, 0, 1, POSMIN, POSMAX);

  posX = i + randPos;
  posY = j + randPos;

  mouse = new PVector(mouseX, mouseY);
  pixel = new PVector(posX, posY);
  distance = mouse.sub(pixel);
}

public void loopControl() {
  /* Verifica si se est\u00e1 en un ciclo y controla el flujo sobre el mismo */
  if (!paused) {
    if (!looping) {
      time += TIMESTEP;
    } else {
      if (loopFwd) {
        if (time <= loopTop) {
          time += TIMESTEP;
        } else {
          loopFwd = !loopFwd;
          time -= TIMESTEP;
        }
      } else {
        if (time >= loopBottom) {
          time -= TIMESTEP;
        } else {
          loopFwd = !loopFwd;
          time += TIMESTEP*2;
        }
      }
    }
  }
}

public void checkInteractions() {
  /* Comprueba interacciones con el usuario */
  if (keyPressed) {
    if (key == 'p' || key == 'P') {
      paused = true;
    }
    if (key == 'r' || key == 'R') {
      paused = false;
    }
    if (key == 'l' || key == 'L') {
      looping = true;
      loopTop = time;
      loopBottom = time - TIMESTEP*(frameRate*2);
    }
    if (key == 'c' || key == 'C') {
      looping = false;
    }
    if (key == CODED) {
      if (keyCode == LEFT) {
        TIMESTEP *= 0.95f;
      }
      if (keyCode == RIGHT) {
        TIMESTEP /= 0.95f;
      }
    }
    if (key == ESC) {
      exit();
    }
  }
  if (mousePressed) {
    if (bgColor == 0) {
      bgColor = 255;
    } else {
      bgColor = 0;
    }
  }
};
  public void settings() {  size(800, 600, FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tarea0_MarcelloAvila" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
