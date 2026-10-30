import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.common.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class chainShape extends PApplet {






Box2DProcessing box2d;

ArrayList<Particle> particles;
Surface surface;

public void setup(){
    
    background(0);

    box2d = new Box2DProcessing(this);
    box2d.createWorld();
    particles = new ArrayList();
    surface = new Surface();
}

public void draw(){
    background(0);
    box2d.step();
    surface.display();
    for(Particle p : particles){
        p.display();
    }
    if(mousePressed){
        if(mouseButton == LEFT){
            particles.add(new Particle(mouseX,mouseY,20));
        }else{
            particles.add(new Particle(mouseX,mouseY,20));
        }
    }
    for(int i = particles.size() -1; i >= 0; i--){
        Particle p = particles.get(i);
        if(p.done()){
            particles.remove(i);
        }
    }
}
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

  public void draw(){
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

  public void createBody(){
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
      fixture.friction = 0.3f;
      fixture.restitution = 0.1f;

      body.createFixture(fixture);
  }

}
class Particle{
    Body body;
    float r;

    Particle(float x, float y, float r){
        this.r = r;
        createBody(x,y);
    }

    public void createBody(float x, float y){
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
        fixture.friction = 0.01f;
        fixture.restitution = 0.8f;

        body.createFixture(fixture);

        body.setLinearVelocity(new Vec2(random(-10,10),random(-10,10)));
        body.setAngularVelocity(random(500));
    }

    public boolean done(){
        Vec2 pos = box2d.getBodyPixelCoord(body);
        if(pos.y > height + r*2){
            killBody();
            return true;
        }
        return false;
    }

    public void killBody() {
        box2d.destroyBody(body);
    }

    public void display(){
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
class Platform extends Box{

  Platform(float x, float y, float w, float h){
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.bodyType = BodyType.STATIC;

      color(128,128,0);
      createBody();
  }
}
class Surface{
    ArrayList<Vec2> points;

    Surface(){
        points = new ArrayList();
        makeBody();
    }

    public void makeBody(){
        BodyDef bd = new BodyDef();
        bd.position.set(0,0);
        Body body = box2d.createBody(bd);

        // Crear lista de puntos en pixeles
        float offset = random(100);
        for(float x = 0; x < width; x += 5){
            float y = noise(offset) * 200 + height / 3 + x * 0.4f;

            points.add(new Vec2(x,y));
            offset += 0.03f;
        }
        float lastYPos = points.get(points.size()-1).y;
        points.add(new Vec2(width, lastYPos + 100));
        points.add(new Vec2(0, lastYPos + 100));

        // Crear lista de puntos en world
        Vec2[] worldPoints = new Vec2[points.size()];
        for (int i = 0; i < points.size(); i++){
            worldPoints[i] = box2d.coordPixelsToWorld(points.get(i));
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldPoints, worldPoints.length);

        body.createFixture(chain, 1);
    }

    public void display() {
        noStroke();
        fill(0,128,128);
        beginShape();
        for(Vec2 p : points){
            vertex(p.x,p.y);
        }
        endShape();
    }
}
  public void settings() {  size(800,600,P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "chainShape" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
