FlowField field;
ArrayList<Vehicle> vehicles;

void setup(){
    size(800,600,FX2D);
    field = new FlowField(25,1);
    vehicles = new ArrayList();
    background(0);
}
void draw(){
    fill(0,30);
    noStroke();
    rect(0,0,width,height);

    //background(0);
    //field.display();
    field.update();
    for(Vehicle v : vehicles){
        PVector f = field.getForce(v.pos.x, v.pos.y);
        v.applyForce(f);
        v.update();
        v.display();
    }
    if(mousePressed && (frameCount%5 == 0)){
        vehicles.add(new Vehicle(mouseX, mouseY, PVector.random2D()));
    }
    if(keyPressed && (key == 'r' || key == 'R')) vehicles.clear();
}
