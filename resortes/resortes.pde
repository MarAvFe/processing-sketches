import controlP5.*;

ControlP5 cp5;

Particle p1, p2;
Spring s;
ArrayList<IAgent> agents;
float margin = 20;
float n = 4;
float mass = 2;
float friction = 0.1;
float k = 0.2;
float restLen = 0;
PVector gravity;
float g = 2;

void setup(){
    size(800,600, P2D);
    background(100,200,100);/*
    p1 = new Particle(width/2-100, height/2, 5, 0.1);
    p2 = new Particle(width/2+100, height/2, 5, 0.1);
    s = new Spring(p1,p2,0.1,150.0);*/
    Particle old = null;
    agents = new ArrayList();
    float sep = (width - 2 * n) / (n-1);
    ArrayList<Particle> particles = new ArrayList();
    for(int i = 0; i < n; i++){
        Particle p = new Particle(i*sep + margin, 100, mass, friction);
        if(i == 0 || i == n-1) p.fix();
        agents.add(p);
        if(i > 0){
            Spring s = new Spring(p, old, k, restLen);
            agents.add(s);
        }
        old = p;
    }
    gravity = new PVector(0, g);
    initControl();
}

void draw(){
    background(0);
    for(IAgent a : agents){
        if(a instanceof Particle){
            Particle p = (Particle)a;
            p.applyForce(gravity);
        }
        a.update();
        a.draw();
    }
}

void setRestLen(float value){
    for(IAgent a : agents){
        if(a instanceof Spring){
            Spring s = (Spring)a;
            s.restLen = value;
        }
    }
}

void initControl(){
    cp5 = new ControlP5(this);
    cp5.addSlider("setRestLen")
        .setPosition(10,10)
        .setSize(300,20)
        .setRange(0,50)
        .setValue(restLen)
        .setCaptionLabel("Rest Length");
}
