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

public class coordenadas_polares extends PApplet {

float ang = 0.0f;
float velAng = 3;
float accAng = 0;
float radio = 0;
int fibInd = 0;
float r,g,b,x,y,colSeed;
int topRadio = 450;
float noiR,noiG,noiB;
boolean wStroke = true;
boolean blurry = false;
int cirSize;
float alph;
public void setup() {
  //size(768, 432);
  //size(1920, 1080);
  
  background(0);
  colSeed = 1;
}

public void draw() {
  pushMatrix();
  translate(width/2, height/2);
  for(int i = 0; i < 50; i++){
      drawPoint();
  }
  popMatrix();
  next();
}

public void drawPoint(){
    x = cos(ang) * radio;
    y = sin(ang) * radio;
    style();
    cirSize = 10;
    if(blurry) cirSize = 60;
    ellipse(x, y, cirSize, cirSize);
    step();
}

public void next(){
    if((x > width/2 && y > height/2) || (x < -(width/2) && y < -(height/2))){
        saveFrame("img/sc-v" + velAng + "-" + (blurry?"blur":"defi") + "-######.png");
        background(0);
        if(blurry){
            radio = 0;
            velAng = fibonacci(fibInd++);
            colSeed += 0.3f;
            blurry = false;
        }else{
            radio = 0;
            blurry = true;
        }
    }
}

public void step(){
    ang += velAng;
    velAng += accAng;
    radio += 0.05f;
}

public void style(){
    noiR = noise(radio*(0.05f),colSeed);
    noiG = noise(radio*(0.01f),colSeed);
    noiB = noise(radio*(0.09f),colSeed);
    r = map(noiR, 0, 1, 0, 255);
    g = map(noiG, 0, 1, 0, 255);
    b = map(noiB, 0, 1, 0, 255);
    if(blurry){
        alph = 5;
    }else{
        alph = map(radio,0,height/2,0,255);
    }
    int col = color(r,g,b,alph);
    fill(col);
    noStroke();
}

public int fibonacci(int n) {
    if (n <= 1) return n;
    else return fibonacci(n-1) + fibonacci(n-2);
}
  public void settings() {  fullScreen(FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "coordenadas_polares" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
