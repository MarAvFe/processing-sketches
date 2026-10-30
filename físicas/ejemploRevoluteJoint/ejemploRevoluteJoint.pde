import shiffman.box2d.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.dynamics.joints.*;

Box2DProcessing box2d;

Surface surface;
ArrayList<Particle> particles;
ArrayList<Bike> bikes;
ArrayList<Platform> plats;
float zone;

void setup() {
    //size(800, 600);
    fullScreen(FX2D);
    box2d = new Box2DProcessing(this);
    box2d.createWorld();
    ArrayList<Vec2> points = new ArrayList();
    surface = new Surface();
    bikes = new ArrayList();
    plats = new ArrayList();
    zone = 100;
}

void draw() {
    background(0);
    box2d.step();
    surface.display();
    for (Bike b : bikes) {
        b.display();
    }
    for (Platform p : plats) {
        p.draw();
    }
    if (mousePressed && (mouseButton == RIGHT)) {
        plats.add(new Platform(mouseX, mouseY, 2, 2));
    }
}

void keyPressed() {
    if (key == 'r' || key == 'R') {
        for (Platform p : plats) {
            p.killBody();
        }
        plats.clear();
    }
    if (key == 'c' || key == 'C') {
        circleZone();
    }
    if (key == CODED) {
      if (keyCode == LEFT) {
        zone -= 10;
      }
      if (keyCode == RIGHT) {
        zone += 10;
      }
    }
}

void mousePressed() {
    if(mouseButton == LEFT){
        Bike b = new Bike(mouseX + random(-40, 40), mouseY + random(-40, 40), random(8,15));
        bikes.add(b);
    }
}

float calcY(float len, float ang){
    return sin(ang)*len;
}

float calcX(float len, float ang){
    return cos(ang)*len;
}

void circleZone(){
    PVector m = new PVector(mouseX,mouseY);
    float granularity = 270;
    println(zone + ": " + granularity);
    for(int i = 0; i < granularity; i++){
        float k = map(i,0,granularity,0,360);
        plats.add(new Platform(m.x+calcX(zone,k), m.y+calcY(zone,k), 2, 2));
    }
};
