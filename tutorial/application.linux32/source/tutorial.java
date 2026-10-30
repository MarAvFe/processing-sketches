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

public class tutorial extends PApplet {


       boolean flag = true;

public void setup() {
       
       stroke(255);
     }
      
     public void draw() {
       line(150, 25, mouseX, mouseY);
     }
     
     public void mousePressed() {
       if (flag){
         background(192, 64, 0);
       }else{
         background(0,128,128);
       }
         flag = !flag;
     }
  public void settings() {  size(400, 400); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "tutorial" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
