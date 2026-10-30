class Walker {

  float x, y, z;
  float noi = 0.005;
  float v;

  Walker(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  void draw() {
    noStroke();
    fill(x, y, z%100+155, 20);

    lights();
    translate(x, y, z);
    sphere(7);
  }

  void update() {
    v = noise(noi);
    v = map(v, 0, 1, 0, 8);
    v = (int)v;
    float stepT = random(30);
    float step = map(stepT, 0, 30, 5, 40);
    if (v==0) x+=step;
    if (v==1) x-=step;
    if (v==6) x+=step; // Facilitar movimiento horizontal
    if (v==7) x-=step; // Facilitar movimiento horizontal
    if (v==2) y+=step;
    if (v==3) y-=step;
    if (v==4) z+=step;
    if (v==5) z-=step;

    if (x > width) {
      x = 0;
    }
    if (x < 0) {
      x = width;
    }
    if (y > height) {
      y = 0;
    }
    if (y < 0) {
      y = height;
    }
    if (z > 50) {
      z = -1000;
    }
    if (z < -1000) {
      z = 50;
    }
    noi += 0.01;
  }
}