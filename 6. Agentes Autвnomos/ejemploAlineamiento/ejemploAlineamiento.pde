ArrayList<Vehicle> vehicles;

void setup() {
  size(800, 600);
  vehicles = new ArrayList();
  background(0);
}

void draw() {
  background(0);
  for (Vehicle v : vehicles) {
    v.align(vehicles);
    v.update();
    v.borders();
    v.display();
  }
}

void mousePressed() {
  Vehicle v = new Vehicle(mouseX, mouseY, PVector.random2D().mult(random(1, 3)), 3, 0.05);
  vehicles.add(v);
}