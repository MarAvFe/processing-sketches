Bola[] bolas;

void setup() {
  size(800, 600);
  bolas = new Bola[5];
  for (int i = 0; i < bolas.length; i++) {
    bolas[i] = new Bola(random(width), random(height));
  }
}

void draw() {
  background(0);
  PVector gravedad = new PVector(0, 0.3);
  PVector viento = new PVector(0.11, 0);
  for (Bola b : bolas) {
    b.aplicarFuerza(gravedad);
    if (mousePressed) {
      b.aplicarFuerza(viento);
    }
    b.actualizar();
    b.bordes();
    b.dibujar();
  }
}