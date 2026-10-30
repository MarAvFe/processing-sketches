class Particle implements IAgent{
    PVector pos, speed, accel;
    float mass, friction;
    color c;
    boolean fixed;

    Particle(float x, float y, float mass, float friction){
        pos = new PVector(x,y);
        speed = new PVector(0,0);
        accel = new PVector(0,0);
        this.mass = mass;
        this.friction = friction;
        c = color(0,128,128,100);
        fixed = false;
    }

    void update(){
        if(!this.fixed){
            calculateFriction();
            pos.add(speed);
            speed.add(accel);
            accel.mult(0);
        }
    }

    void draw(){
        noStroke();
        fill(c);
        ellipse(pos.x,pos.y,mass*5,mass*5);
    }

    void applyForce(PVector force){
        PVector f = PVector.div(force,mass);
        speed.add(f);
    }

    void fix(){
        this.fixed = true;
    }

    void unfix(){
        this.fixed = false;
    }

    void calculateFriction(){
        PVector f = speed.copy();
        f.normalize();
        f.mult(-friction);
        applyForce(f);
    }
}
