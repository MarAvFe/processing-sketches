public class Bola {
  PVector posicion;
  PVector velocidad;
  PVector aceleracion;
  float masa, rndR, rndG, rndB;
  Bola(float x, float y) {
    posicion = new PVector(x, y);
    velocidad = new PVector(0, 0);
    aceleracion = new PVector(0, 0);
    masa = random(10, 25);
    rndR = random(10,120);
    rndG = random(80,120);
    rndB = random(130,250);
  }
  void dibujar() {
    fill(rndR, rndG, rndB);
    noStroke();
    ellipse(posicion.x, posicion.y, masa, masa);
  }
  void actualizar() {
    velocidad.add(aceleracion);
    posicion.add(velocidad);
    aceleracion.mult(0);
  }
  void aplicarFuerza(PVector fuerza) {
    PVector f = PVector.div(fuerza, masa);
    aceleracion.add(f);
  }
}