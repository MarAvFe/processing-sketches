void setup() {
  size(800, 600);
}

void draw() {
  background(0);
  PVector origin = new PVector(100, height / 2);
  PVector mouse = new PVector(mouseX, mouseY);
  PVector v1 = new PVector(600, 0);
  PVector v2 = PVector.sub(mouse, origin);
  
  pushMatrix();
  translate(origin.x, origin.y);
  stroke(255);
  strokeWeight(3);
  line(0, 0, v1.x, v1.y);
  line(0, 0, v2.x, v2.y);
  PVector v3 = v1.copy();
  v1.normalize();
  float dot = v1.dot(v2);
  v3.setMag(dot);

  PVector d = PVector.sub(v2, v3);
  float dist = d.mag();
  
  fill(255, 0, 0);
  ellipse(v3.x, v3.y, 10, 10);
  strokeWeight(1);
  line(v2.x, v2.y, v3.x, v3.y);
  
  popMatrix();
  
  fill(255);
  textSize(15);
  text("Distancia: " + dist, 10, height - 50);
}