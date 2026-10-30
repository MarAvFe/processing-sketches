import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AstronomyBitch extends PApplet {

Walker w;

public void setup()
{
  
  background(0);
  noCursor();
}

public void draw()
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
class Walker
{
  float x;
  float y;
  Walker(float y , float x)
  {
    this.x = x;
    this.y = y;
  }
  
  public void render()
  {
    point(x,y);
  }
  
  public void update()
  {
    float value = random(100);
    if(value < 15) x++;
    if(value < 30) x--;
    if(value < 50) y++;
    if(value < 99 ) y++;
    //if(value == 4) { x++; y--;}
    //if(value == 5) { x--; y++;}
    //if(value == 6) { x++; y++;}
    //if(value == 7) { x--; y--;}
  }
  
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AstronomyBitch" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
