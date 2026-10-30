class Box {
  Body body;
  float w, h;
  color c;
  
  Box(float x, float y, float w, float h) {
    this.w = w;
    this.h = h;
    makeBody(x, y);
    c = color(random(255), 0, random(255));
  }
  void makeBody(float x, float y) {
    BodyDef bd = new BodyDef();
    bd.position.set(box2d.coordPixelsToWorld(x, y));
    bd.type = BodyType.STATIC;
    body = box2d.world.createBody(bd);

    PolygonShape ps = new PolygonShape();
    ps.setAsBox(box2d.scalarPixelsToWorld(w / 2), box2d.scalarPixelsToWorld(h / 2));    
    
    FixtureDef fd = new FixtureDef();
    fd.setShape(ps);
    fd.setDensity(1);
    fd.setFriction(0.01);
    fd.setRestitution(0.5);
    
    body.createFixture(fd);
  }
  void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    fill(c);
    stroke(255);
    strokeWeight(3);
    rectMode(CENTER);
    rect(0, 0, w, h);
    popMatrix();
  }
}