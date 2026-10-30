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
}
