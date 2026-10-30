Bola[] bolas;
PVector[] fuerzas;

void setup() {
  size(800, 600, FX2D);
  bolas = new Bola[200];
  for (int i = 0; i < bolas.length; i++) {
    bolas[i] = new Bola(random(width-100)+50, random(height-100)+50);
    bolas[i].aplicarFuerza(new PVector(randomGaussian()*4,randomGaussian()*4));
  }
}

void draw() {
  background(0,0,0,50);
  PVector viento = new PVector(0.5, 0);
  for (Bola b : bolas) {
    float MAXSPEED = 0.7;
    PVector right = new PVector(map(b.posicion.x,0,width,MAXSPEED,0),0);
    PVector left = new PVector(map(b.posicion.x,width,0,-MAXSPEED,0),0);
    PVector down = new PVector(0,map(b.posicion.y,0,height,MAXSPEED,0));
    PVector up = new PVector(0,map(b.posicion.y,height,0,-MAXSPEED,0));
    if (mousePressed) {
      b.aplicarFuerza(viento);
    }
    b.aplicarFuerza(right);
    b.aplicarFuerza(left);
    b.aplicarFuerza(up);
    b.aplicarFuerza(down);
    b.actualizar();
    b.dibujar();
  }
}
