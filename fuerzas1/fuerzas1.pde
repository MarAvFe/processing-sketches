Bola b;

void setup() {
  size(800, 600);
  b = new Bola(width / 2, height / 2);
}

void draw() {
  background(0);
  PVector fuerza = new PVector(0.1, 0.02);
  b.aplicarFuerza(fuerza);
  b.actualizar();
  b.bordes();
  b.dibujar();
}