class Hub{
  int max, remaining;
  float barLength;
  Hub(int max){
    this.max = max;
    remaining = max;
    barLength = width * 0.2;
  }
  
  void draw(){
    fill(100,0,0,200);
    rect(10,10,barLength,20);
    fill(0,0,0,200);
    rect(11,11,map(remaining,0,max,0,barLength-2),18);
  }
  
  void update(int value){
    remaining += value;
  }
}