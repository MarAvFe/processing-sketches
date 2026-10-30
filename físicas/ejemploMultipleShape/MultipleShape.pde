class MultipleShape {
  Body body;
  float w, h, r;
  color c1, c2, c3;

  MultipleShape(float x, float y) {
    w = 10;
    h = 40;
    r = 5;
    makeBody(x, y);
    c1 = color(random(255), random(255), random(255));
    c2 = color(random(255), random(255), random(255));
    c3 = c2;
  }
  void makeBody(float x, float y) {
    BodyDef bd = new BodyDef();
    bd.setType(BodyType.DYNAMIC);
    bd.setPosition(box2d.coordPixelsToWorld(x, y));
    bd.setAngle(random(PI));
    body = box2d.createBody(bd);

    // crear el rectángulo
    PolygonShape rectangle = new PolygonShape();
    float box2dW = box2d.scalarPixelsToWorld(w / 2);
    float box2dH = box2d.scalarPixelsToWorld(h / 2);
    rectangle.setAsBox(box2dW, box2dH);

    FixtureDef rectangleFixture = new FixtureDef();
    rectangleFixture.setShape(rectangle);
    rectangleFixture.setDensity(1);
    rectangleFixture.setRestitution(0.5);
    rectangleFixture.setFriction(0.2);

    body.createFixture(rectangleFixture);

    // crear primer círculo
    CircleShape circle1 = new CircleShape();
    circle1.setRadius(box2d.scalarPixelsToWorld(r));
    Vec2 offset1 = box2d.vectorPixelsToWorld(-w / 2 - r, 0);
    circle1.m_p.set(offset1.x, offset1.y);

    FixtureDef circle1Fixture = new FixtureDef();
    circle1Fixture.setShape(circle1);
    circle1Fixture.setDensity(1);
    circle1Fixture.setRestitution(0.5);
    circle1Fixture.setFriction(0.2);

    body.createFixture(circle1Fixture);

    // crear segundo círculo
    CircleShape circle2 = new CircleShape();
    circle2.setRadius(box2d.scalarPixelsToWorld(r));
    Vec2 offset2 = box2d.vectorPixelsToWorld(w / 2 + r, 0);
    circle2.m_p.set(offset2.x, offset2.y);

    FixtureDef circle2Fixture = new FixtureDef();
    circle2Fixture.setShape(circle2);
    circle2Fixture.setDensity(1);
    circle2Fixture.setRestitution(0.5);
    circle2Fixture.setFriction(0.2);

    body.createFixture(circle2Fixture);

    body.setLinearVelocity(new Vec2(random(-10, 10), random(-5, 5)));
    body.setAngularVelocity(random(-10, 10));
  }
  boolean done() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    if (pos.y > height) {
      killBody();
      return true;
    }
    return false;
  }
  void killBody() {
    box2d.destroyBody(body);
  }
  void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    noStroke();
    fill(c1);
    rect(0, 0, w, h);
    fill(c2);
    ellipse(-w / 2 - r, 0, r * 2, r * 2);
    fill(c3);
    ellipse(w / 2 + r, 0, r * 2, r * 2);
    popMatrix();
  }
}