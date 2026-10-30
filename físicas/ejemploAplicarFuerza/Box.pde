class Box {
  Body body;
  float w, h, x, y;
  color c;
  Box(float x, float y, float w, float h) {
    this.w = w;
    this.h = h;
    this.x = x;
    this.y = y;
    makeBody(x, y);
    c = color(random(255), 0, random(255));
  }
  void makeBody(float x, float y) {
    BodyDef bd = new BodyDef();
    bd.type = BodyType.DYNAMIC;
    bd.position.set(box2d.coordPixelsToWorld(x, y));
    body = box2d.createBody(bd);
    
    PolygonShape ps = new PolygonShape();
    ps.setAsBox(box2d.scalarPixelsToWorld(w/2), box2d.scalarPixelsToWorld(h/2));
    
    FixtureDef fd = new FixtureDef();
    fd.setShape(ps);
    fd.setDensity(1);
    fd.setFriction(0.5);
    fd.setRestitution(0.5);
    
    body.createFixture(fd);
    body.setLinearVelocity(new Vec2(random(-25, 25), random(0, 25)));
    body.setAngularVelocity(random(-5, 5));
  }
  void killBody() {
    box2d.destroyBody(body);
  }
  boolean mouseOver() {
    Vec2 worldPos = box2d.coordPixelsToWorld(mouseX, mouseY);
    Fixture f = body.getFixtureList();
    return f.testPoint(worldPos);
  }
  void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    x = pos.x;
    y = pos.y;
    float a = body.getAngle();
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    fill(c);
    stroke(255);
    strokeWeight(3);
    rect(0, 0, w, h);
    popMatrix();
  }
  void applyForce(Vec2 force) {
    Vec2 pos = body.getWorldCenter();
    body.applyForce(force, pos);
  }
  void attractToCenter() {
    Vec2 pos = body.getWorldCenter();
    Vec2 center = box2d.coordPixelsToWorld(new Vec2(width / 2, height / 2));
    Vec2 force = pos.sub(center);
    float distance = force.length();
    distance = constrain(distance, 1, 5);
    force.normalize();
    float strength = -500 * body.m_mass / (distance * distance);
    force.mulLocal(strength);
    applyForce(force);    
  }
}