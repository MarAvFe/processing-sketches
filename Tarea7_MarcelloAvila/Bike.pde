class Bike {
    Particle big, small;
    Box base;
    Joint bigJoint, smallJoint;
    float w;
    float h;
    float radius;
    boolean ded;

    Bike(float x, float y, float radius) {
        ded = false;
        w = 3*radius + radius*1.5;
        h = radius*0.5;
        base = new Box(x, y, w, h);
        small = new Particle(x-w/2, y, radius, 100);
        big = new Particle(x+w/2, y, radius*1.5, 50);

        RevoluteJointDef bigJointRev = new RevoluteJointDef();
        bigJointRev.initialize(base.body, big.body, big.body.getTransform().p);
        bigJointRev.motorSpeed = radius * 0.615;
        bigJointRev.maxMotorTorque = 50000;
        bigJointRev.enableMotor = true;

        RevoluteJointDef smallJointRev = new RevoluteJointDef();
        smallJointRev.initialize(base.body, small.body, small.body.getTransform().p);
        smallJointRev.motorSpeed = 2 * radius * 0.615;
        smallJointRev.maxMotorTorque = 50000;
        smallJointRev.enableMotor = true;

        bigJoint = box2d.world.createJoint(bigJointRev);
        smallJoint = box2d.world.createJoint(smallJointRev);
    }
    void display() {
        if(!ded){
            base.display();
            big.display();
            small.display();
        }
    }
    void killBody() {
        box2d.world.destroyJoint(bigJoint);
        box2d.world.destroyJoint(smallJoint);
        box2d.destroyBody(base.body);
        box2d.destroyBody(small.body);
        box2d.destroyBody(big.body);
        ded = true;
    }
    boolean done() {
        Vec2 basePos = box2d.getBodyPixelCoord(base.body);
        Vec2 smallPos = box2d.getBodyPixelCoord(small.body);
        Vec2 bigPos = box2d.getBodyPixelCoord(big.body);
        if (basePos.y > height && smallPos.y > height && bigPos.y > height) {
            killBody();
            return true;
        }
        return false;
    }
}
