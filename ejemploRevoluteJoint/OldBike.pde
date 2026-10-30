class OldBike {
    PolygonParticle bigWheel;
    PolygonParticle smallWheel;
    float bw = 10, sw = 5;
    Box base;
    Joint joint;
    float w = 100;
    float h = 20;

    OldBike(float x, float y) {
        base = new Box(x, y, w, h);

        CircleShape bigWheel = new CircleShape();
        bigWheel.setRadius(box2d.scalarPixelsToWorld(bw));

        CircleShape smallWheel = new CircleShape();
        smallWheel.setRadius(box2d.scalarPixelsToWorld(sw));

        frontJoin = new Vec2(x - 40, y);
        backJoin = new Vec2(x + 40, y);

        RevoluteJointDef rjd = new RevoluteJointDef();
        rjd.initialize(bigWheel.body, base.body, bigWheel.body.getTransform().p);
        rjd.motorSpeed = PI * 1000;
        rjd.enableMotor = true;

        joint = box2d.world.createJoint(rjd);
    }
    void display() {
        base.display();
        spike.display();
    }
}
