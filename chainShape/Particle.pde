class Particle{
    Body body;
    float r;

    Particle(float x, float y, float r){
        this.r = r;
        createBody(x,y);
    }

    void createBody(float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(box2d.coordPixelsToWorld(x,y));
        bodyDef.setAngle(random(PI));
        body = box2d.createBody(bodyDef);

        CircleShape cs = new CircleShape();
        cs.setRadius(box2d.scalarPixelsToWorld(r));

        FixtureDef fixture = new FixtureDef();
        fixture.setShape(cs);
        fixture.density = 1;
        fixture.friction = 0.01;
        fixture.restitution = 0.8;

        body.createFixture(fixture);

        body.setLinearVelocity(new Vec2(random(-10,10),random(-10,10)));
        body.setAngularVelocity(random(500));
    }

    boolean done(){
        Vec2 pos = box2d.getBodyPixelCoord(body);
        if(pos.y > height + r*2){
            killBody();
            return true;
        }
        return false;
    }

    void killBody() {
        box2d.destroyBody(body);
    }

    void display(){
        Vec2 pos = box2d.getBodyPixelCoord(body);
        float a = body.getAngle();
        pushMatrix();
        translate(pos.x,pos.y);
        rotate(-a);
        fill(random(0,255),random(0,255),random(0,255),random(0,150));
        stroke(255);
        strokeWeight(2);
        ellipse(0,0,r*2,r*2);
        line(-r,0,r,0);
        line(0,-r,0,r);
        popMatrix();
    }

}
