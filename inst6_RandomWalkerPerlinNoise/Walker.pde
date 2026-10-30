class Walker{

  float x, y;
  float noi = 0.005;
  
  Walker(float x, float y){
    this.x = x;
    this.y = y;
  }
  
  void draw(){
    stroke(255,255,255);
    point(x,y);
    stroke(0,128,128,50);
    point(x+1,y+1);
    point(x+1,y-1);
    point(x-1,y+1);
    point(x-1,y-1);
    
    point(x+1,y);
    point(x-1,y);
    point(x,y+1);
    point(x,y-1);
  }
  
  void update(){
    float s = noise(noi);
    s = map(s, 0, 1, -10, 10);
    println(s);
    int v = (int)random(8);
    if (v==0) x+=s;
    if (v==1) x-=s;
    if (v==2) y+=s;
    if (v==3) y-=s;
    if (v==4) {
      x+=s;
      y+=s;
    }
    if (v==5) {
      x+=s;
      y-=s;
    }
    if (v==6) {
      x-=s;
      y+=s;
    }
    if (v==7) {
      x-=s;
      y-=s;
    }
    noi += 0.1;
    if(x > width){x = 0; s = 0;}
    if(x < 0){x = width; s = width;}
    if(y > height){y = 0; s = 0;}
    if(y < 0){y = height; s = height;}
  }
  
  

}