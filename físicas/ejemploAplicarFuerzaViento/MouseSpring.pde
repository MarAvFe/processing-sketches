class MouseSpring {
  MouseJoint mouseJoint;
  MouseSpring() {
    mouseJoint = null;
  }
  void update() {
    if (mouseJoint != null) {
      Vec2 mouseWorld = box2d.coordPixelsToWorld(mouseX, mouseY);
      mouseJoint.setTarget(mouseWorld);
    }
  }
  void bind(Box box) {
    MouseJointDef mjd = new MouseJointDef();
    mjd.bodyA = box2d.getGroundBody();
    mjd.bodyB = box.body;
    Vec2 mp = box2d.coordPixelsToWorld(mouseX, mouseY);
    mjd.target.set(mp);
    mjd.maxForce = 1000 * box.body.m_mass;
    mjd.frequencyHz = 5;
    mjd.dampingRatio = 1;
    
    mouseJoint = (MouseJoint)box2d.world.createJoint(mjd);
  }
  void unbind() {
    if (mouseJoint != null) {
      box2d.world.destroyJoint(mouseJoint);
      mouseJoint = null;
    }
  }
}