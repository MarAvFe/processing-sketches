class NoiseSurface extends Surface {
  NoiseSurface(color c, float base, float h, float noiseStep, float pointDistance) {
    this.x = 0;
    this.y = 0;
    this.c = c;
    points = new ArrayList();
    float offset = random(100);
    for (float x = 0; x < width; x += pointDistance) {
      float y = noise(offset) * h + base;
      Vec2 point = new Vec2(x, y);
      points.add(point);
      offset += noiseStep;
    }
    points.add(new Vec2(width, height));
    points.add(new Vec2(0, height));
    box2DInit();
  }
}