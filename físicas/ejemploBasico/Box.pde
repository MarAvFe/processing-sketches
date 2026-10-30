class Box {
  float x, y;
  float w, h;
  Body body;

  Box(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.STATIC;
    bodyDef.position.set(box2d.coordPixelsToWorld(x, y));
    body = box2d.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    float box2dW = box2d.scalarPixelsToWorld(w/2);
    float box2dH = box2d.scalarPixelsToWorld(h/2);
    shape.setAsBox(box2dW, box2dH);

    FixtureDef fixture = new FixtureDef();
    fixture.shape = shape;
    fixture.density = 1;
    fixture.friction = 0.3;
    fixture.restitution = 0.5;

    body.createFixture(fixture);
  }
  void draw() {
    fill(0, 180, 255);
    stroke(255);
    rectMode(CENTER);

    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    rect(0, 0, w, h);
    popMatrix();
  }
}
