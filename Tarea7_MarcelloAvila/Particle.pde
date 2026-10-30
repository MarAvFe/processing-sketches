class Particle {
  Body body;
  float r;
  float friction;

  Particle() {}
  Particle(float x, float y, float r, float f) {
    this.r = r;
    friction = f;
    makeBody(x, y);
  }
  void makeBody(float x, float y) {
    BodyDef bd = new BodyDef();
    bd.position = box2d.coordPixelsToWorld(x, y);
    bd.type = BodyType.DYNAMIC;
    body = box2d.world.createBody(bd);

    CircleShape cs = new CircleShape();
    cs.setRadius(box2d.scalarPixelsToWorld(r));

    FixtureDef fd = new FixtureDef();
    fd.setShape(cs);
    fd.setDensity(1);
    fd.setFriction(friction);
    fd.setRestitution(0);

    body.createFixture(fd);
  }
  void killBody() {
    box2d.destroyBody(body);
  }
  boolean done() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    if (pos.y > height) {
      killBody();
      return true;
    }
    return false;
  }
  void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    fill(0,0,0,map(r,4,28,255,0));
    stroke(255,255,0);
    strokeWeight(1);
    ellipse(0, 0, r * 2, r * 2);
    line(-r, 0, r, 0);
    line(0, -r, 0, r);
    popMatrix();
  }
}
