class Box extends Shape {
  float w, h;
  float angle;
  float density;
  float friction;
  float restitution;
  Box(float x, float y, color c, float w, float h, float angle, float density, float friction, float restitution) {
    this.x = x;
    this.y = y;
    this.c = c;
    this.w = w;
    this.h = h;
    this.angle = angle;
    this.density = density;
    this.friction = friction;
    this.restitution = restitution;
    box2DInit();
  }
  void createBodyDef() {
    super.createBodyDef();
    def.setAngle(angle);
    type = def.type = BodyType.DYNAMIC;
  }
  void createShape() {
    PolygonShape s = new PolygonShape();
    float ww = box2d.scalarPixelsToWorld(w/2);
    float wh = box2d.scalarPixelsToWorld(h/2);
    s.setAsBox(ww, wh);
    shape = s;
  }
  void createFixtureDef() {
    super.createFixtureDef();
    fixture.density = density;
    fixture.friction = friction;
    fixture.restitution = restitution;
  }
  void draw() {
    fill(c);
    stroke(0);
    rectMode(CENTER);
    Vec2 pos = box2d.getBodyPixelCoord(body);
    angle = body.getAngle();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-angle);
    rect(0, 0, w, h);
    popMatrix();
  }
}