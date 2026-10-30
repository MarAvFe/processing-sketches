class Box {
  float x, y;
  float w, h;
  color c;
  Body body;
  BodyType type;

  Box() {}
  Box(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.type = BodyType.DYNAMIC;
    c = color(255, 0, 0);
    createBody();
  }
  void createBody() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = type;
    bodyDef.position.set(box2d.coordPixelsToWorld(x, y));
    bodyDef.setAngle(random(PI));
    bodyDef.setAngularVelocity(20);
    body = box2d.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    float box2dW = box2d.scalarPixelsToWorld(w/2);
    float box2dH = box2d.scalarPixelsToWorld(h/2);
    shape.setAsBox(box2dW, box2dH);

    FixtureDef fixture = new FixtureDef();
    fixture.shape = shape;
    fixture.density = 1;
    fixture.friction = 10;
    fixture.restitution = 0.4;

    body.createFixture(fixture);
  }
  void draw() {
    fill(0, 180, 255);
    stroke(255);
    fill(c);
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
