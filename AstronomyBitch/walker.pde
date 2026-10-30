class Walker
{
  float x;
  float y;
  Walker(float y , float x)
  {
    this.x = x;
    this.y = y;
  }
  
  void render()
  {
    point(x,y);
  }
  
  void update()
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