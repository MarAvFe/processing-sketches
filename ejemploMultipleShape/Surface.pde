class Surface {
  ArrayList<Vec2> points;

  Surface() {
    points = new ArrayList();
    makeBody();
  }
  void makeBody() {
    BodyDef bd = new BodyDef();
    bd.position.set(0, 0); // la superficie no necesita posición
    Body body = box2d.createBody(bd); // no es necesario guardar la referencia a body porque nunca se va a mover

    // Crear lista de puntos en pixeles
    ChainShape chain = new ChainShape();
    for (float x = 0; x < width/2; x += 100) {
      float y = height * 2/3 + random(40);
      points.add(new Vec2(x, y));
    }
    float lastYPos = points.get(points.size() - 1).y;
    float lastXPos = points.get(points.size() - 1).x;
    points.add(new Vec2(lastXPos, lastYPos + 100));
    points.add(new Vec2(0, lastYPos + 100));
    // Crear lista de puntos en world
    Vec2[] worldPoints = new Vec2[points.size()];
    for (int i = 0; i < points.size(); i++) {
      worldPoints[i] = box2d.coordPixelsToWorld(points.get(i));
    }
    chain.createChain(worldPoints, worldPoints.length);

    // Se pueden crear fixtures directamente a partir de una forma
    // El parámetro 1 es la densidad
    body.createFixture(chain, 1);
  }
  void display() {
    noStroke();
    fill(255);
    beginShape();
    for (Vec2 p : points) {
      vertex(p.x, p.y);
    }
    endShape();
  }
}