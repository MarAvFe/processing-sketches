public class Water {
  float watTopLevel;
  float watLevel;
  float watTideVar;
  float watDensity;
  
  Water() {
    watTopLevel = height * 0.8;
    watLevel = 0;
    watTideVar = 0.08;
    watDensity = 1;
    stroke(255);
    line(0,watTopLevel,200,watTopLevel);
  }
  
  void draw() {
    fill(30,25,70);
    noStroke();
    rect(0, watTopLevel+watLevel, width, height);
    this.update();
  }
  
  void update(){
    watLevel = map(noise(TIME), 0, 1, -height * watTideVar,height * watTideVar);
  }
  
  PVector drag(Bullet b){
    float speed = b.speed.mag();
    PVector strength = b.speed.copy();
    strength.mult(-1);
    strength.setMag(watDensity * speed * speed);
    return strength;
  }
  
  boolean contains(Bullet b){
    if(b.pos.y > watTopLevel+watLevel){
      return true;
    }
    return false;
  }
} 