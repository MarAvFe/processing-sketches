public class Bola {
  PVector posicion;
  PVector velocidad;
  PVector aceleracion;
  float masa;
  color col;
  Bola(float x, float y) {
    posicion = new PVector(x, y);
    velocidad = new PVector(0, 0);
    aceleracion = new PVector(0, 0);
    masa = randomGaussian()*5 + 20;
    col = color(0,0,random(100,255),40);
  }
  void dibujar() {
    noStroke();
    fill(col);
    ellipse(posicion.x, posicion.y, masa * 20, masa * 20);
  }
  void bordes() {
    if (posicion.x < 0 || posicion.x > width) {
      velocidad.x *= -1;
      posicion.x = constrain(posicion.x, 0, width);
    }
    if (posicion.y < 0 || posicion.y > height) {
      velocidad.y *= -1;
      posicion.y = constrain(posicion.y, 0, height);
    }
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
