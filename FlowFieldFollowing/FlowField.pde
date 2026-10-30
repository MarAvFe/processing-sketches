class FlowField{
    PVector[][] grid;
    float resolution;
    int rows;
    int columns;
    float defaultMag = 5;

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
                float angle = map(noise(c*0.5, r*0.1, frameCount*0.005),0,1,0,TWO_PI);
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
        stroke(255,255z,0);
        strokeWeight(2);
        line(0,0,v.x,v.y);
        ellipse(v.x,v.y,3,3);
        popMatrix();
    }
    PVector getForce(float x, float y){
        if(x >= 0 && x <= width){
            if(y >= 0 && y <= height){
                int r = (int)(y/resolution);
                int c = (int)(x/resolution);
                return grid[r][c];
            }
        }
        return new PVector(0,0);
    }
}
