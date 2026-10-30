class Walker{

  float x, y;
  
  Walker(float x, float y){
    this.x = x;
    this.y = y;
  }
  
  void draw(){
    point(x,y);
  }
  
  void update(){
    int v = (int)random(4);
    
  }

}