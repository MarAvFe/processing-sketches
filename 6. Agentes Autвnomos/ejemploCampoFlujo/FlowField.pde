class FlowField {
  PVector [][] grid;
  float resolution;
  int rows;
  int columns;
  float noiseOffset = 0.1;
  float defaultMag = 5;
  
  FlowField(float resolution, float noiseOffset, float defaultMag) {
    this.resolution = resolution;
    this.noiseOffset = noiseOffset;
    this.defaultMag = defaultMag;
    rows = (int)(height / resolution);
    columns = (int)(width / resolution);
    grid = new PVector[rows][];
    for (int r = 0; r < rows; r++) {
      grid[r] = new PVector[columns];
      for (int c = 0; c < columns; c++) {
        float angle = map(noise((float)r * noiseOffset, (float)c * noiseOffset), 0, 1, 0, TWO_PI);
        grid[r][c] = PVector.fromAngle(angle);
        grid[r][c].setMag(defaultMag);
      }
    }
  }
  void display() {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {
        displayVector(grid[r][c], c * resolution, r * resolution);
      }
    }
  }
  void displayVector(PVector vector, float x, float y) {
    PVector v = vector.copy();
    v.setMag(resolution / 2);
    pushMatrix();
    translate(x + resolution/2, y + resolution/2);
    stroke(128);
    line(0, 0, v.x, v.y);
    popMatrix();    
  }
  PVector getForce(float x, float y) {
    if (x >= 0 && x <= width) {
      if (y >= 0 && y <= height) {
        int r = (int)(y / resolution);
        int c = (int)(x / resolution);
        return grid[r][c];
      }
    }
    return new PVector(0, 0);
  }
}