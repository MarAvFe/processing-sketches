public class Bola {
  PVector posicion;
  PVector velocidad;
  PVector aceleracion;
  float masa, rndR, rndG, rndB;
  boolean pull;
  color colr;
  Bola(float x, float y, boolean pull) {
    posicion = new PVector(x, y);
    velocidad = new PVector(0, 0);
    aceleracion = new PVector(0, 0);
    this.pull = pull;
    if(this.pull){
        rndR = random(10,120);
        rndG = random(80,120);
        rndB = random(130,250);
        colr = color(rndR, rndG, rndB);
    }else{
        rndR = random(130,250);
        rndG = random(80,120);
        rndB = random(10,20);
        colr = color(rndR, rndG, rndB);
    }
    masa = random(100,200);
  }
  void draw() {
    fill(colr);
    noStroke();
    ellipse(posicion.x, posicion.y, 10, 10);
  }
  void update() {
    velocidad.add(aceleracion);
    posicion.add(velocidad);
    aceleracion.mult(0);
  }
  void applyForce(PVector fuerza) {
    PVector f = PVector.div(fuerza, masa);
    aceleracion.add(f);
  }
}
