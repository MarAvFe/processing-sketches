Walker w;

void setup()
{
  size(800, 600);
  background(0);
  noCursor();
}

void draw()
{
  background(0);
  stroke(255);
  fill(255);

  PVector mouse = new PVector(mouseX, mouseY);
  PVector centro = new PVector(width/2, height/2);
  ellipse(mouse.x, mouse.y, 10, 10);
  ellipse(centro.x, centro.y, 10, 10);
  PVector dif = PVector.sub(mouse,centro);
  dif.mult(-1);

  PVector dif2 = PVector.sub(mouse,centro);
  dif2.normalize();
  dif2.mult(100);
  translate(width/2,height/2);

  line(0,0, dif.x, dif.y);
  line(0,0, dif2.x, dif2.y);

  noFill();
  ellipseMode(RADIUS);
  ellipse(0,0,100,100);
  ellipse(0,0,dif.mag(),dif.mag());
}
