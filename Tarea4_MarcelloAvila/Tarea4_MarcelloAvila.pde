/*
 Simulación de Sistemas Naturales
 Marcello Ávila 
 2014084118
 
 Tarea 4 - Cañón
 
 Descripción:
   Un cañón dirigido por el mouse que dispara balas con el click del mouse.
   Las balas experimentan fricción contra el aire o fuerza de arrastre según
     el entono en que se encuentren. Además traen un movimiento angular 
     dirigido por el ángulo del disparo. Los colores de estas son aleatorios.
   El indicador izquierdo superior muestra la cantidad de balas restantes.
   
 */
float GRAVITY = 0.2;
float WINDFRICTIONCOEF = -1;
float TIME = 0.0;

int numBullets = 60;
PVector mouse;
PVector gGravity;
Water water;
Canon canon;
Hub hub;

void setup() {
  size(800, 600, FX2D);
  gGravity = new PVector(0, GRAVITY);
  water = new Water();
  canon = new Canon(water);
  hub = new Hub(numBullets);
  background(0);
}

void draw() {
  fill(0,0,0,80);
  rect(0,0,width,height);
  water.draw();
  canon.update();
  canon.draw();
  hub.draw();
  TIME += 0.01;
}