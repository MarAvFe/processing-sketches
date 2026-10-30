class Spring {
  Particle p1;
  Particle p2;
  Joint joint;
  float length;
  float frequencyHz;
  float dampingRatio;

  Spring(Particle p1, Particle p2, float length, float frequencyHz, float dampingRatio) {
    this.p1 = p1;
    this.p2 = p2;
    this.length = length;
    this.frequencyHz = frequencyHz;
    this.dampingRatio = dampingRatio;
    makeJoint();
  }
  void makeJoint() {
    DistanceJointDef djd = new DistanceJointDef();
    djd.bodyA = p1.body;
    djd.bodyB = p2.body;
    djd.length = box2d.scalarPixelsToWorld(length);
    djd.frequencyHz = frequencyHz;
    djd.dampingRatio = dampingRatio;
    joint = box2d.world.createJoint(djd);    
  }
  void display() {
    Vec2 pos1 = box2d.getBodyPixelCoord(p1.body);
    Vec2 pos2 = box2d.getBodyPixelCoord(p2.body);
    stroke(255);
    strokeWeight(3);
    line(pos1.x, pos1.y, pos2.x, pos2.y);
  }
}