class Box{
  float x, y;
  float w, h;
  Body body;
  BodyType bodyType;

  Box(){}
  Box(float x, float y, float w, float h){
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.bodyType = BodyType.DYNAMIC;

      color(0,32,32);
      createBody();
  }

  void draw(){
      fill(0, 180, 255);
      noStroke();
      rectMode(CENTER);
      //rect(x,y,w,h);

      Vec2 pos = box2d.getBodyPixelCoord(body);
      float a = body.getAngle();
      pushMatrix();
      translate(pos.x,pos.y);
      rotate(-a);
      rect(0,0,w,h);
      popMatrix();
  }

  void createBody(){
      BodyDef bodyDef = new BodyDef();
      bodyDef.type = bodyType;
      bodyDef.position.set(box2d.coordPixelsToWorld(x,y));
      bodyDef.setAngle(random(PI));
      body = box2d.createBody(bodyDef);

      PolygonShape shape = new PolygonShape();
      float box2dW = box2d.scalarPixelsToWorld(w/2);
      float box2dH = box2d.scalarPixelsToWorld(h/2);
      shape.setAsBox(box2dW, box2dH);

      FixtureDef fixture = new FixtureDef();
      fixture.shape = shape;
      fixture.density = 1;
      fixture.friction = 0.3;
      fixture.restitution = 0.1;

      body.createFixture(fixture);
  }

}
