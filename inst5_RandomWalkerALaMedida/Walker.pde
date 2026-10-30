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
    point(x+2,y+2);
    point(x-1,y-1);
    point(x-2,y-2);
  }
  
  void update(){
    int s = (int)monteCarlo() * 5 + 3;
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
  
  float monteCarlo() {
    float prob;
    while (true) {
      float r1 = random(1);
      // Lineal
      //prob = r1;
      
      // Exponencial
      //prob = r1 * r1;
      
      // Logaritmica
      prob = log(r1 * 2.7182 + 1);
      
      // Hiperbolica
      //prob = 0.1 / r1;
      
      float r2 = random(1);
      if(r2 < prob) {
        return r1;
      }
    }
  }
}