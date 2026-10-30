ArrayList<Vehicle> vehicles;

void setup() {
  size(800, 600);
  vehicles = new ArrayList();
  background(0);
}

void draw() {
  background(0);
  for (Vehicle v : vehicles) {
    v.flock(vehicles);
    v.update();
    v.borders();
    v.display();
  }
  if (mousePressed) {
    Vehicle v = new Vehicle(mouseX, mouseY, PVector.random2D(), 1, 0.05);
    vehicles.add(v);
  }
}
