FlowField field;
ArrayList<Vehicle> vehicles;

void setup() {
  //size(800, 600);
  fullScreen(FX2D, 2);
  background(0);
  field = new FlowField(25, 0.2, 1, 0.01);
  vehicles = new ArrayList();
}

void draw() {
  background(0);
  field.update();
  field.display();
  for (Vehicle v : vehicles) {
    PVector f = field.getForce(v.pos.x, v.pos.y);
    v.follow(f);
    v.update();
    v.display();
  }
  if (mousePressed) {
    Vehicle v = new Vehicle(mouseX, mouseY, PVector.random2D());
    v.setWrapBorders(true);
    vehicles.add(v);
    
  }
}