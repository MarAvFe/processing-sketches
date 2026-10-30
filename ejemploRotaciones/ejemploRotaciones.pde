Bola[] bolas;

void setup() {
  size(800, 600,FX2D);
  bolas = new Bola[10];
  for (int i = 0; i < bolas.length; i++) {
    bolas[i] = new Cuadro(random(width), random(height));
  }
  background(0);
}

void draw() {
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
