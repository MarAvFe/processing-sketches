/*
 Simulación de Sistemas Naturales
 Marcello Ávila
 2014084118

 Tarea 9 - Guided flocking

 Descripción:
    Se crea un camino con el mouse, al presionar ENTER se cierra el ciclo y con el
      click se crearán vehículos que sigan la ruta trazada pero tengan comportamiento
      de flocking, con una mayor prioridad en la característica de separación.
    Con R se reinician los vehículos.
    Con Q se reinicia el camino.
    Con G se guardan los recuadros.
    Con P se pausa el movimiento de los vehículos
*/

ArrayList<Vehicle> vehicles;
Path p;
boolean settingPoints = true;
boolean pause = false;
boolean save = false;

void setup() {
  //size(800, 600);
  fullScreen(FX2D);
  vehicles = new ArrayList();
  p = new Path();
  background(0);
}

void draw() {
  if (!pause) {
    background(0);
    p.display();
    for (Vehicle v : vehicles) {
      v.flock(vehicles);
      v.follow(p);
      v.update();
      v.borders();
      v.display();
    }
  }
  if (mousePressed) {
    if (settingPoints) {
      p.addPoint(mouseX, mouseY);
    } else {
      float maxSpeed = 3;
      Vehicle v = new Vehicle(mouseX, mouseY, PVector.random2D().mult(maxSpeed), maxSpeed, 0.5);
      vehicles.add(v);
    }
  }
  if (save) {
    saveFrame("img\\####.png");
  }
}
void keyPressed() {
  if (key == '\n') {
    settingPoints = false;
  }
  if ((key == 'p') || (key == 'P')) {
    pause = !pause;
  }
  if ((key == 'g') || (key == 'G')) {
    save = !save;
  }
  if ((key == 'R') || (key == 'r')) {
    vehicles.clear();
  }
  if ((key == 'q') || (key == 'Q')) {
    p = new Path();
    settingPoints = true;
  }
}
