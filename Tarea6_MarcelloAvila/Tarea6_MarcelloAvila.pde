/*
 Simulación de Sistemas Naturales
 Marcello Ávila
 2014084118

 Tarea 6 - Resortes atractivos

 Los círculos azules repelen y los rojos atraen.
   Se generan aleatoriamente.
 Con click derecho se agregan nuevo sistemas de partículas para
   evitar conflicto al momento de manipular los controles.
 Por alguna razón no pude inicializar bien los valores de algunos
   de los controles, entonces aparecen con números muy pequeños.
   Algunos de los controles van de 1-100, pero a la hora de aplicar
   la función, tienen un map, por lo que no son los valores reflejados
   en la interfaz. Sin ninguna razón particular.

*/
import controlP5.*;
ControlP5 cp5;

ParticleSystem s;
float g = 0.002;
PVector gravity;
float friction = 0.02;
float k = 0.01;
float lifeSpan = 300;
float particleMass = 10;
float restLen = 100;

float G = 0.05;

ArrayList<ParticleSystem> systems;
Bola[] pullers;
int NUMPULLERS = 25;

void setup() {
  size(800, 600);
  background(0);
  systems = new ArrayList();
  gravity = new PVector(0, g);
  pullers = new Bola[NUMPULLERS];
  for (int i = 0; i < pullers.length; i++) {
    pullers[i] = new Bola(random(0, width), random(0, height), random(2)>1?true:false);
  }
  setGravity(g);
  setFriction(friction);
  setParticleMass(particleMass);
  initControl();
}

void draw() {
  fill(0, 0, 0, 30);
  noStroke();
  rect(0, 0, width, height);
  for (ParticleSystem s : systems) {
    s.applyForce(gravity);
    s.update();
    s.draw();
    if (frameCount % 20 == 0) s.addParticle();
  }
  for (Bola p : pullers) {
    p.draw();
    p.update();
  }

  if (mousePressed && (mouseButton == RIGHT)) {
    systems.add(new ParticleSystem(mouseX, mouseY));
  }
}

void setGravity(float value) {
  gravity = new PVector(0, value);
}

void setFriction(float value) {
  friction = value;
}

void setParticleMass(float value) {
  particleMass = map(value,0,100,2,30);
}

void setK(float value) {
  k = map(value,0,100,0.005,1.2);
}

void setLife(float value) {
  lifeSpan = map(value,0,100,30,500);
}

void setRestLen(float value) {
  restLen = map(value,0,100,2,30);
}

void initControl() {
  cp5 = new ControlP5(this);
  cp5.addSlider("setGravity")
    .setPosition(10, 10)
    .setSize(200, 20)
    .setRange(0, 0.05)
    .setValue(g)
    .setCaptionLabel("Gravedad");

  cp5.addSlider("setFriction")
    .setPosition(10, 40)
    .setSize(200, 20)
    .setRange(0, 0.5)
    .setValue(friction)
    .setCaptionLabel("Friccion");

  cp5.addSlider("setParticleMass")
    .setPosition(10, 70)
    .setSize(200, 20)
    .setRange(1, 100)
    .setValue(particleMass)
    .setCaptionLabel("Masa");

  cp5.addSlider("setK")
    .setPosition(10, 100)
    .setSize(200, 20)
    .setRange(0, 2)
    .setValue(k)
    .setCaptionLabel("Elasticidad");

  cp5.addSlider("setLife")
    .setPosition(10, 130)
    .setSize(200, 20)
    .setRange(1, 100)
    .setValue(lifeSpan)
    .setCaptionLabel("Vida");

  cp5.addSlider("setRestLen")
    .setPosition(10, 160)
    .setSize(200, 20)
    .setRange(1, 100)
    .setValue(restLen)
    .setCaptionLabel("Longitud");
}
