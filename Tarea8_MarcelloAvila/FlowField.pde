class FlowField{
    PVector[][] grid;
    float resolution;
    int rows;
    int columns;
    float defaultMag;

    FlowField(float resolution, float defaultMag){
        this.resolution = resolution;
        this.defaultMag = defaultMag;
        rows = (int)(height / resolution);
        columns = (int)(width / resolution);
        initGrid();
        update();
    }
    void initGrid(){
        grid = new PVector[rows][];
        for (int r = 0; r < rows; r++){
            grid[r] = new PVector[columns];
        }
    }
    void update(){
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < columns; c++){
                float angle = map(noise(c*0.1, r*0.1, frameCount*0.0025),0,1,-TWO_PI,TWO_PI);
                grid[r][c] = PVector.fromAngle(angle);
                grid[r][c].setMag(defaultMag);
            }
        }
    }
    void display(){
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < columns; c++){
                displayVector(grid[r][c], c * resolution, r * resolution);
            }
        }
    }
    void displayVector(PVector vector, float x, float y){
        PVector v = vector.copy();
        v.setMag(resolution/2);
        pushMatrix();
        translate(x + resolution / 2, y + resolution / 2);
        stroke(100,0,100,90);
        strokeWeight(1);
        line(0,0,v.x,v.y);
        ellipse(v.x,v.y,2,2);
        popMatrix();
    }
    PVector getForce(float x, float y){
        if(x >= 0 && x <= width-1){
            if(y >= 0 && y <= height-1){
                int r = (int)(y/resolution);
                int c = (int)(x/resolution);
                return grid[r][c];
            }
        }
        return new PVector(0,0);
    }
}
