Walker w;

void setup(){
  size(600,400);
  background(0);
  frameRate(500);
  w = new Walker(width/2, height/2);
}

void draw(){
  w.update();
  w.draw();
}