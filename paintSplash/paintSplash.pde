void setup(){
  size(600,400);
  background(50);
  frameRate(30);
}

void draw(){
  if(mousePressed){
    float s = randomGaussian() * 10 + 10;
    
    float dx = randomGaussian() * 20;
    float dy = randomGaussian() * 20;
    
    float r = randomGaussian() * 155 + 85;
    float g = randomGaussian() * 155 + 50;
    float b = randomGaussian() * 155 + 100;
    
    fill(color(r,g,b));
    ellipse(mouseX+dx,mouseY+dy,s,s);
  }
}