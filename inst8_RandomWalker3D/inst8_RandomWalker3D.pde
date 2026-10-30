Walker w;

void setup() {
  size(400, 600, P3D);  // Specify P3D renderer
  background(0);

  stroke(255);

  // Superficie
  line(30, 500, 20, 30, 500, -1000);
  line(30, 500, 20, 370, 500, 20);
  line(370, 500, -1000, 370, 500, 20);
  line(370, 500, -1000, 30, 500, -1000);

  // Pared izq
  line(30, 200, -1000, 30, 500, -1000);
  line(30, 200, -1000, 30, 200, 20);
  line(30, 200, 20, 30, 500, 20);

  // Pared fondo
  line(30, 200, -1000, 370, 200, -1000);
  line(370, 500, -1000, 370, 200, -1000);

  w = new Walker(30, 500, -500);
}

void draw() {
  w.draw();
  w.update();
}