class Walker{

  float x, y;
  
  Walker(float x, float y){
    this.x = x;
    this.y = y;
  }
  
  void draw(){
    stroke(255,255,255);
    point(x,y);
    stroke(255,255,255,80);
    point(x+1,y+1);
    point(x+1,y-1);
    point(x-1,y+1);
    point(x-1,y-1);
  }
  
  void update(){
    int s = (int)randomGaussian() * 5 + 1;
    int v = (int)random(8);
    if (v==0) x+=s;
    if (v==1) x-=s;
    if (v==2) y+=s;
    if (v==3) y-=s;
    if (v==4) {
      x+=s;
      this.draw();
      y+=s;
    }
    if (v==5) {
      x+=s;
      this.draw();
      y-=s;
    }
    if (v==6) {
      x-=s;
      this.draw();
      y+=s;
    }
    if (v==7) {
      x-=s;
      this.draw();
      y-=s;
    }
    if(x > width){x = 0;}
    if(x < 0){x = width;}
    if(y > height){y = 0;}
    if(y < 0){y = height;}
  }
  
  

}