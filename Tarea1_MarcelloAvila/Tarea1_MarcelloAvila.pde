/*
 Simulación de Sistemas Naturales
 Marcello Ávila
 2014084118

 Tarea 1 - Globos de Helio

 Descripción:
   Cada globo inicia con dirección aleatoria y magnitud.
   Existe una rafaga de viento constante hacia la izquierda y al
     hacer click con el mouse, esta cambia de dirección y aumenta
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

void setup() {
  size(800, 600, FX2D);
  for (int i = 0; i < NUMBALLS; i++) {
    float posX = map(random(100), 0, 100, 250, width-250);
    float posY = map(random(100), 0, 100, 350, height-50);
    bolas.add(new Ball(posX, posY, PVector.random2D()));
  }
}

void draw() {
  if (!ended) {
    background(10, 120, 180);
    PVector viento;
    for (Ball b : bolas) {
      windStr = noise(b.pos.x, b.pos.y, time);
      windStr = map(windStr, 0, 1, 0, 0.03);
      viento = new PVector(windStr, 0);
      if (mousePressed) {
        // Viento izquierda
        windStr = map(windStr, 0, 1, 0.1, 0.2);
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
    time += 0.01;
    poppedBalls = 0;
  } else {
    textSize(50);
    fill(0, 102, 153);
    text("Por qué sos así? :(", width*0.1, height*0.2);
  }
}
