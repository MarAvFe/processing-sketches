/*
 Simulación de Sistemas Naturales
 Marcello Ávila 
 2014084118
 
 Tarea 0 - Matriz de círculos en pantalla
 
 Acciones:
   (ESC)ape      : Detener simulación
   (P)ause       : Pausar tiempo
   (R)esume      : Continuar tiempo
   (L)oop        : Iniciar ciclo de 2 segundos en tiempo normal
   (C)ontinue    : Salir del ciclo y continuar simulación
   (LEFT)        : Acelerar tiempo
   (RIGHT)       : Ralentizar tiempo
   (MOUSE)       : Invierte el color de los pixeles cercanos
   (CLICK)       : Invierte el color del fondo de pantalla
 
 Parametrización:
   PIXELBASE     : Define la media donde oscila el tamaño de cada pixel
   TIMESTEP      : Cantidad que se aumenta el tiempo en cada cambio de cuadro
   varios        : Todas las variables en mayúscula
 */

float TIMESTEP = 0.01;
float PIXELBASE = 10;

float REDNOISE = 0.5;
float GREENNOISE = 2;
float BLUENOISE = 5;

float SIZENOISE = 4;
float SIZEMIN = 1;
float SIZEMAX = PIXELBASE * 2.5;

float POSNOISE = 0.8;
float POSMIN = -50;
float POSMAX = 50;

/* ---------- Terminan parámetros ---------- */

int bgColor = 0;
float time = 0.0;
boolean paused = false;
boolean looping = false;
boolean loopFwd = true;
float loopTop, loopBottom;

float randRed, randGreen, randBlue, randSize, randPos, posX, posY;
PVector mouse, pixel, distance;
int i, j;

void setup() {
  size(800, 600, FX2D);
  background(0);
  //ellipseMode(RADIUS);
  noStroke();
}

void draw() {
  background(bgColor);
  for (i = 0; i < width; i+=PIXELBASE) {
    for (j = 0; j < height; j+=PIXELBASE) {

      update();

      fill(randRed, randGreen, randBlue);
      if ((PIXELBASE*2 > abs(distance.x)) &&(PIXELBASE*2 > abs(distance.y)) ) {
        // Si el pixel está muy cerca del mouse, invierta el color
        fill(255-randRed, 255-randGreen, 255-randBlue);
      }

      ellipse(posX, posY, randSize, randSize);
    }
  }

  loopControl();
  checkInteractions();
}

void update() {
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

void loopControl() {
  /* Verifica si se está en un ciclo y controla el flujo sobre el mismo */
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

void checkInteractions() {
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
        TIMESTEP *= 0.95;
      }
      if (keyCode == RIGHT) {
        TIMESTEP /= 0.95;
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