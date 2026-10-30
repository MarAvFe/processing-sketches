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
        this.c = color(255,0,0,200);
    }

    void update(){
        PVector springForce = PVector.sub(p2.pos, p1.pos);
        springForce.normalize();
        springForce.mult(-k * getDisplacement());
        springForce.div(2);
        p2.applyForce(springForce);
        springForce.mult(-1);
        p1.applyForce(springForce);


    }

    float getDisplacement(){
        return PVector.sub(p2.pos, p1.pos).mag() - restLen;
    }

    void draw(){
        pushStyle();
        stroke(c);
        float strength = map(getDisplacement()*k,0,restLen*10*k,0.1,3);
          strokeWeight(strength);
        
        line(p1.pos.x,p1.pos.y,p2.pos.x,p2.pos.y);
        popStyle();
    }
}