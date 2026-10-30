class FlowField {
  PVector [][] grid;
  float resolution;
  int rows;
  int columns;
  float noiseResolution;
  float directionNoiseOffset;
  float directionChangeSpeed;
  float forceNoiseOffset;
  float forceChangeSpeed;
  float maxMag;

  FlowField(float resolution, float noiseResolution, float maxMag, float directionChangeSpeed, float forceChangeSpeed) {
    this.resolution = resolution;
    this.noiseResolution = noiseResolution;
    this.directionNoiseOffset = random(100);
    this.directionChangeSpeed = directionChangeSpeed;
    this.forceNoiseOffset = random(100);
    this.forceChangeSpeed = forceChangeSpeed;
    this.maxMag = maxMag;
    rows = (int)(height / resolution);
    columns = (int)(width / resolution);
    initGrid();
    updateVectors();
  }
  void initGrid() {
    grid = new PVector[rows][];
    for (int r = 0; r < rows; r++) {
      grid[r] = new PVector[columns];
    }
  }
  void updateVectors() {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {
        float angle = map(noise((float)r * noiseResolution, (float)c * noiseResolution, directionNoiseOffset), 0, 1, 0, TWO_PI);
        float mag = map(noise((float)r * noiseResolution, (float)c * noiseResolution, forceNoiseOffset), 0, 1, 0, maxMag);
        grid[r][c] = PVector.fromAngle(angle);
        grid[r][c].setMag(mag);
      }
    }
    directionNoiseOffset += directionChangeSpeed;
    forceNoiseOffset += forceChangeSpeed;
  }
  void update() {
    updateVectors();
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
    rectMode(CENTER);
    noStroke();
    colorMode(HSB);
    float hue = map(vector.mag(), 0, maxMag, 128, 0);
    color c = color(hue, 255, 255, 128);
    fill(c);
    colorMode(RGB);
    translate(x + resolution/2, y + resolution/2);
    rect(0, 0, resolution, resolution);
    strokeWeight(1);
    stroke(128);
    line(0, 0, v.x, v.y);
    fill(255);
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