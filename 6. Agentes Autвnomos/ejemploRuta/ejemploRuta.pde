ArrayList<Vehicle> vehicles;
Path p;

void setup() {
  size(800, 600);
  vehicles = new ArrayList();
  p = new Path(0, height/2 + random(-100, 100), width, height/2 + random(-100, 100));
  background(0);
}

void draw() {
  background(0);
  p.display();
  for (Vehicle v : vehicles) {
    v.follow(p);
    v.update();
    v.borders();
    v.display();
  }
}

void mousePressed() {
  Vehicle v = new Vehicle(mouseX, mouseY, PVector.random2D(), 3, 0.1);
  vehicles.add(v);
}