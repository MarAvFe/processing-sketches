class Spring implements IAgent{

    Particle p1;
    Particle p2;
    float k, restLen;
    color c;

    Spring(Particle p1, Particle p2, float k, float restLen){
        this.p1 = p1;
        this.p2 = p2;
        this.k = k;
        this.restLen = restLen;
        this.c = color(255);
    }

    void update(){
        PVector springForce = PVector.sub(p2.pos, p1.pos);
        float len = springForce.mag();
        springForce.normalize();
        float displacement = len - restLen;
        springForce.mult(-k * displacement);
        springForce.div(2);
        p2.applyForce(springForce);
        springForce.mult(-1);
        p1.applyForce(springForce);
    }

    void draw(){
        stroke(c);
        line(p1.pos.x,p1.pos.y,p2.pos.x,p2.pos.y);
    }
}
