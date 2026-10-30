public class Bola {
  private PVector posicion;
  private PVector velocidad;
  private PVector aceleracion;
  private float masa;
  public Bola(float x, float y) {
    posicion = new PVector(x, y);
    velocidad = new PVector(0, 0);
    aceleracion = new PVector(0, 0);
    masa = random(1, 2);
  }
  public void dibujar() {
    stroke(255);
    fill(128);
    strokeWeight(3);
    ellipse(posicion.x, posicion.y, masa * 20, masa * 20);
  }
  public void bordes() {
    if (posicion.x < 0 || posicion.x > width) {
      velocidad.x *= -1;
      posicion.x = constrain(posicion.x, 0, width);
    }
    if (posicion.y < 0 || posicion.y > height) {
      velocidad.y *= -1;
      posicion.y = constrain(posicion.y, 0, height);
    }
  }
  public void actualizar() {
    velocidad.add(aceleracion);
    posicion.add(velocidad);
    aceleracion.mult(0);
  }
  public void aplicarFuerza(PVector fuerza) {
    PVector f = PVector.div(fuerza, masa);
    aceleracion.add(f);
  }
}