class Surface {
  ArrayList<Vec2> points;

  Surface(ArrayList<Vec2> points) {
    this.points = points;
    makeBody();
  }
  void makeBody() {
    BodyDef bd = new BodyDef();
    bd.position.set(0, 0); // la superficie no necesita posición
    Body body = box2d.createBody(bd); // no es necesario guardar la referencia a body porque nunca se va a mover

    // Crear lista de puntos en pixeles
    ChainShape chain = new ChainShape();
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