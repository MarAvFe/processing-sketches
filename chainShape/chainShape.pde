import shiffman.box2d.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;

Box2DProcessing box2d;

ArrayList<Particle> particles;
Surface surface;

void setup(){
    size(800,600,FX2D);
    background(0);

    box2d = new Box2DProcessing(this);
    box2d.createWorld();
    particles = new ArrayList();
    surface = new Surface();
}

void draw(){
    background(0);
    box2d.step();
    surface.display();
    for(Particle p : particles){
        p.display();
    }
    if(mousePressed){
        if(mouseButton == LEFT){
            particles.add(new Particle(mouseX,mouseY,20));
        }else{
            particles.add(new Particle(mouseX,mouseY,20));
        }
    }
    for(int i = particles.size() -1; i >= 0; i--){
        Particle p = particles.get(i);
        if(p.done()){
            particles.remove(i);
        }
    }
}
