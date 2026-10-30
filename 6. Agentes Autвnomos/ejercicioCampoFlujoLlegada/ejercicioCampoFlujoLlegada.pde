FlowField field;
ArrayList<Vehicle> vehicles;

void setup() {
  size(800, 600);
  background(0);
  field = new FlowField(20, 0.2, 1, 0.01, 0.05);
  vehicles = new ArrayList();
}

void draw() {
  background(0);
  field.update();
  field.display();
  for (Vehicle v : vehicles) {
    PVector f = field.getForce(v.pos.x, v.pos.y);
    println(f);
    v.applyForce(f);
    v.update();
    v.display();
  }
  if (mousePressed) {
    Vehicle v = new Vehicle(mouseX, mouseY, PVector.random2D());
    v.setWrapBorders(true);
    vehicles.add(v);    
  }
  fill(255, 0, 0, 100);
  noStroke();
  ellipse(mouseX, mouseY, 20, 20);
}