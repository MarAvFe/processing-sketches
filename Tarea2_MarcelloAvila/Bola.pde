public class Bola {
  PVector posicion;
  PVector velocidad;
  PVector aceleracion;
  float masa, randRed, randBlue, randGreen;
  Bola(float x, float y) {
    posicion = new PVector(x, y);
    velocidad = new PVector(0, 0);
    aceleracion = new PVector(0, 0);
    masa = random(1,3);
  }
  void dibujar() {
    stroke(255-randRed, 255-randGreen, 255-randBlue);
    strokeWeight(0.3);
    line(width/2, height/2, posicion.x, posicion.y);

    fill(randRed, randGreen, randBlue, 150);
    strokeWeight(masa);
    ellipse(posicion.x, posicion.y, masa * 20, masa * 20);
  }

  void actualizar() {
    velocidad.add(aceleracion);
    posicion.add(velocidad);
    aceleracion.mult(0);
    float REDNOISE = 1;
    float BLUENOISE = 2;
    float GREENNOISE = 0.5;
    randRed = noise(map(posicion.x, 0, width, 0, REDNOISE), map(posicion.y, 0, height, 0, REDNOISE));
    randRed = abs(map(randRed, 0, 1, 0, 255));

    randGreen = noise(map(posicion.x, 0, width, 0, GREENNOISE), map(posicion.y, 0, height, 0, GREENNOISE));
    randGreen = abs(map(randGreen, 0, 1, -255, 255));

    randBlue = noise(map(posicion.x, 0, width, 0, BLUENOISE), map(posicion.y, 0, height, 0, BLUENOISE));
    randBlue = abs(map(randBlue, 0, 1, -255, 255));
  }
  void aplicarFuerza(PVector fuerza) {
    PVector f = PVector.div(fuerza, masa);
    aceleracion.add(f);
  }
}