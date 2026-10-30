class Walker{

  float x, y;
  
  Walker(float x, float y){
    this.x = x;
    this.y = y;
  }
  
  void draw(){
    stroke(255,255,255,40);
    point(x,y);
  }
  
  void update(){
    int v = (int)random(8);
    if (v==0) x++;
    if (v==1) x--;
    if (v==2) y++;
    if (v==3) y--;
    if (v==4) {
      x++;
      y++;
    }
    if (v==5) {
      x++;
      y--;
    }
    if (v==6) {
      x--;
      y++;
    }
    if (v==7) {
      x--;
      y--;
    }
  }

}