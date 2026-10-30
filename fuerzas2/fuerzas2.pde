Bola b;

void setup() {
  size(800, 600);
  b = new Bola(width / 2, height / 2);
}

void draw() {
  background(0);
  PVector gravedad = new PVector(0, 0.3);
  PVector viento = new PVector(0.11, 0);
  b.aplicarFuerza(gravedad);
  b.aplicarFuerza(viento);
  b.actualizar();
  b.bordes();
  b.dibujar();
}