// Marcello Ávila
// Se hace viento constante desde el mouse hacia cada partícula, al hacer click.
// Por alguna razón se descontrolan las partículas con
//   algunas combinaciones de parámetros
// El grosor de los resortes cambia según su tensión.


import controlP5.*;

ControlP5 cp5;

Particle p1, p2;
Spring s;
ArrayList<IAgent> agents;
float margin = height*0.2;
float n = 10;
float h = 10;
float k = 4.5;
float g = 9.8;
float mass = 8;
float friction = 2;
PVector gravity;

float leftBorder, rightBorder, topBorder, bottomBorder, restLen;

void setup(){
    //size(800,600, P2D);
    fullScreen(P2D);
    background(100,200,100);
    Particle left = null;
    agents = new ArrayList();
    float x = width  / n;
    float y = height / h;
    leftBorder = width*0.2;
    rightBorder = width-margin;
    topBorder = height*0.2;
    bottomBorder = height-margin;
    restLen = (rightBorder-leftBorder)/n/5;

    ArrayList<Particle> tops = new ArrayList<Particle>();
    for(int i = 0; i < n; i++) tops.add(new Particle(0,0,0,0));

    for(int j = 0; j < h; j++){
        for(int i = 0; i < n; i++){
            Particle p = new Particle(
                map(i*x,0,(n-1)*x,leftBorder,rightBorder),
                map(j*y,0,(h-1)*y,topBorder,bottomBorder),
                mass, friction);
                if(i == 0 && j == 0) p.fix();
                //if(i == 0 && j == h-1) p.fix();
                if(i == n-1 && j == 0) p.fix();
                //if(i == n-1 && j == h-1) p.fix();
                agents.add(p);

                if(i > 0){
                    Spring s = new Spring(p, left, k, restLen);
                    agents.add(s);
                }
                left = p;

                if(j > 0){
                    Spring s = new Spring(p, tops.get(i), k, restLen);
                    agents.add(s);
                }
                tops.set(i,p);
            }
            left = null;
        }
        setGravity(g);
        initControl();
    }

    void draw(){
        background(0);

        rectMode(CORNERS);
        fill(150,0,150,100);
        rect(leftBorder,topBorder,rightBorder,bottomBorder);

        for(IAgent a : agents){
            if(a instanceof Particle){
                Particle p = (Particle)a;
                p.applyForce(gravity);
                if (mousePressed) {
                    PVector mouse = new PVector(mouseX,mouseY);
                    PVector viento = PVector.sub(mouse,p.pos);
                    viento.normalize();
                    viento.setMag(-10);
                    p.applyForce(viento);
                }
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

    void setK(float value){
        for(IAgent a : agents){
            if(a instanceof Spring){
                Spring s = (Spring)a;
                s.k = value;
            }
        }
    }

    void setFriction(float value){
        for(IAgent a : agents){
            if(a instanceof Particle){
                Particle p = (Particle)a;
                p.friction = value;
            }
        }
    }

    void setMass(float value){
        for(IAgent a : agents){
            if(a instanceof Particle){
                Particle p = (Particle)a;
                p.mass = value;
            }
        }
    }

    void setGravity(float value){
        gravity = new PVector(0, value);
    }

    void initControl(){
        cp5 = new ControlP5(this);
        cp5.addSlider("setRestLen")
        .setPosition(10,10)
        .setSize(200,20)
        .setRange(restLen/10,restLen*2)
        .setValue(restLen)
        .setCaptionLabel("Rest Length");

        cp5.addSlider("setFriction")
        .setPosition(10,40)
        .setSize(200,20)
        .setRange(-2,5)
        .setValue(friction)
        .setCaptionLabel("Friction");

        cp5.addSlider("setK")
        .setPosition(10,70)
        .setSize(200,20)
        .setRange(1,16)
        .setValue(k)
        .setCaptionLabel("Elasticity");

        cp5.addSlider("setMass")
        .setPosition(10,100)
        .setSize(200,20)
        .setRange(2,20)
        .setValue(mass)
        .setCaptionLabel("Mass");

        cp5.addSlider("setGravity")
        .setPosition(10,130)
        .setSize(200,20)
        .setRange(-5,20)
        .setValue(g)
        .setCaptionLabel("Gravity");
    }
