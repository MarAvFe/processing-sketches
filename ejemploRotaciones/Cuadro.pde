class Cuadro extends Bola {
  float ang;
  float velAng;
  float accAng;
color col;
  Cuadro(float x, float y){
    super(x, y);
    ang = random(2*PI);
    velAng = 0;
    accAng = 0;
    col = color(random(0,255),0,random(100,255),20);
  }

  void dibujar(){
    ang += velAng;
    velAng += accAng;
    accAng = map(velocidad.mag()/3, 0, 46, -0.001, 0.001);
    pushMatrix();
    translate(posicion.x, posicion.y);
    rotate(ang);
    fill(col);
    rectMode(CENTER);
    noStroke();
    rect(0, 0, masa, masa);
    popMatrix();
  }

}
