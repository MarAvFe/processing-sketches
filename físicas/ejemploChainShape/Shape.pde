abstract class Shape {
  float x, y;
  color c;
  Body body;
  BodyType type;
  BodyDef def;
  org.jbox2d.collision.shapes.Shape shape;
  FixtureDef fixture;
  
  Shape() {}
  Shape(float x, float y, color c) {
    this.x = x;
    this.y = y;
    this.c = c;
    box2DInit();
  }
  void box2DInit() {
    createBodyDef();
    createBody();
    this.createShape();
    createFixtureDef();
    createFixture();
  }
  void createBodyDef() {
    def = new BodyDef();
    def.type = type;
    def.position.set(box2d.coordPixelsToWorld(x, y));    
  }
  void createBody() {
    body = box2d.createBody(def);
  }
  abstract void createShape();
  void createFixtureDef() {
    fixture = new FixtureDef();
    fixture.shape = shape;
    fixture.density = 1;
    fixture.friction = 0;
    fixture.restitution = 0.5;
  }
  void createFixture() {
    body.createFixture(fixture);
  }
  abstract void draw();
}