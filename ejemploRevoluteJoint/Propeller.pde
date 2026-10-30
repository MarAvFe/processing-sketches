class Propeller {
  PolygonParticle spike;
  Box base;
  Joint joint;
  float w = 10;
  float h = 180;

  Propeller(float x, float y) {
    base = new Box(x, y - h/2, w, h);
    spike = new PolygonParticle(x, y - h + w/2)

    RevoluteJointDef rjd = new RevoluteJointDef();
    rjd.initialize(base.body, spike.body, spike.body.getTransform().p);
    rjd.motorSpeed = PI * 1000;
    rjd.enableMotor = true;

    joint = box2d.world.createJoint(rjd);
  }
  void display() {
    base.display();
    spike.display();
  }
}
