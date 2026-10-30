float ang = 0.0;
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
void setup() {
  //size(768, 432);
  //size(1920, 1080);
  fullScreen(FX2D);
  background(0);
  colSeed = 1;
}

void draw() {
  pushMatrix();
  translate(width/2, height/2);
  for(int i = 0; i < 50; i++){
      drawPoint();
  }
  popMatrix();
  next();
}

void drawPoint(){
    x = cos(ang) * radio;
    y = sin(ang) * radio;
    style();
    cirSize = 10;
    if(blurry) cirSize = 60;
    ellipse(x, y, cirSize, cirSize);
    step();
}

void next(){
    if((x > width/2 && y > height/2) || (x < -(width/2) && y < -(height/2))){
        saveFrame("img/sc-v" + velAng + "-" + (blurry?"blur":"defi") + "-######.png");
        background(0);
        if(blurry){
            radio = 0;
            velAng = fibonacci(fibInd++);
            colSeed += 0.3;
            blurry = false;
        }else{
            radio = 0;
            blurry = true;
        }
    }
}

void step(){
    ang += velAng;
    velAng += accAng;
    radio += 0.05;
}

void style(){
    noiR = noise(radio*(0.05),colSeed);
    noiG = noise(radio*(0.01),colSeed);
    noiB = noise(radio*(0.09),colSeed);
    r = map(noiR, 0, 1, 0, 255);
    g = map(noiG, 0, 1, 0, 255);
    b = map(noiB, 0, 1, 0, 255);
    if(blurry){
        alph = 5;
    }else{
        alph = map(radio,0,height/2,0,255);
    }
    color col = color(r,g,b,alph);
    fill(col);
    noStroke();
}

int fibonacci(int n) {
    if (n <= 1) return n;
    else return fibonacci(n-1) + fibonacci(n-2);
}
