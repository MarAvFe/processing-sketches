class Path {
  PVector start;
  PVector end;
  float radius;
  
  Path(float x1, float y1, float x2, float y2) {
    radius = 15;
    start = new PVector(x1, y1);
    end = new PVector(x2, y2);
  }
  void display() {
    strokeWeight(radius * 2);
    stroke(207, 247, 137);
    line(start.x, start.y, end.x, end.y);
    strokeWeight(1);
    stroke(0);
    line(start.x, start.y, end.x, end.y);
  }
}