/*
 Simulación de Sistemas Naturales
 Marcello Ávila
 2014084118

 Tarea 3 - Estrellas (?)

 Descripción:
   Se crean 600 estrellas y todas ejercen una fuerza repelente sobre las demás.
   Cada una tiene una masa variable entre 10 y 40.
   Existe un sol, guiado por el mouse, con una masa de 10.000, que ejerce
     una fuerza de atracción por las estrellas.
   Al hacer click, el sol desprende una onda expansiva donde repele a todas
     las estrellas dentro de un rango de alcance variable.
 */
Bola[] bolas;
PImage bg;

int stars = 600;
int sunSize = 40;
int sunMass = 10000;
float wave = 2.6;
float G = 0.0005;
PVector mouse;
PVector pull;

void setup() {
  size(800, 600, FX2D);
  bolas = new Bola[stars];
  for (int i = 0; i < bolas.length; i++) {
    bolas[i] = new Bola(random(0, width), random(0, height));
  }
  background(0);
  bg = loadImage("space.png");
}

void draw() {
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

    int w = int(sunSize * wave);
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
