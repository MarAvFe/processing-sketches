class Surface extends Shape {
  ArrayList<Vec2> points;
  
  Surface() {}
  Surface(color c, ArrayList<Vec2> points) {
    // La superficie no necesita una posición
    this.x = 0;
    this.y = 0;
    this.c = c;
    this.points = points;
    box2DInit();
  }
  void createBodyDef() {
    super.createBodyDef();
    type = def.type = BodyType.STATIC;
  }
  void createShape() {
    ChainShape chain = new ChainShape();
    Vec2[] chainPoints = new Vec2[points.size()];
    for (int i = 0; i < points.size(); i++) {
      chainPoints[i] = box2d.coordPixelsToWorld(points.get(i).x + width / 2, points.get(i).y + height / 2);
    }
    chain.createChain(chainPoints, chainPoints.length);
    shape = chain;
  }
  void draw() {
    noStroke();
    fill(c);
    beginShape();
    for (Vec2 p : points) {
      vertex(p.x, p.y);
    }
    endShape();
  }  
}