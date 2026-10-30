class Platform extends Box {
  Platform(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    type = BodyType.STATIC;
    c = color(0, 255, 0);
    createBody();
  }
}