class PolygonParticle {
  Body body;
  ArrayList<Vec2> vertices;
  color c;

  PolygonParticle() {
  }
  PolygonParticle(float x, float y) {
    vertices = new ArrayList();

    vertices.add(new Vec2(0, -120));
    vertices.add(new Vec2(10, 5));
    vertices.add(new Vec2(-10, 5));

    makeBody(x, y);
    c = color(random(255), 0, random(255));
  }
  void makeBody(float x, float y) {
    PolygonShape ps = new PolygonShape();
    Vec2[] worldVertices = new Vec2[vertices.size()];
    for (int i = 0; i < vertices.size(); i++) {
      worldVertices[i] = box2d.vectorPixelsToWorld(vertices.get(i));
    }
    ps.set(worldVertices, worldVertices.length);

    FixtureDef fd = new FixtureDef();
    fd.setShape(ps);
    fd.setDensity(1);
    fd.setRestitution(0.7);
    fd.setFriction(0);
    BodyDef bd = new BodyDef();
    bd.setPosition(box2d.coordPixelsToWorld(x, y));
    bd.setType(BodyType.DYNAMIC);
    body = box2d.createBody(bd);
    body.createFixture(fd);
  }

  void display() {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    PolygonShape ps = (PolygonShape) body.getFixtureList().getShape();
    float a = body.getAngle();
    rectMode(CENTER);
    stroke(255);
    strokeWeight(3);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    fill(c);
    beginShape();
    for (int i = 0; i < ps.getVertexCount(); i++) {
      Vec2 v = box2d.vectorWorldToPixels(ps.getVertex(i));
      vertex(v.x, v.y);
    }
    endShape(CLOSE);
    popMatrix();
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
}