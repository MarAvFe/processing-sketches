import java.util.Iterator;

class ParticleSystem {
    PVector origin;
    ArrayList<PairedParticle> particles;

    ParticleSystem(float x, float y){
        origin = new PVector(x, y);
        particles = new ArrayList();
    }

    void update(){
        Iterator<PairedParticle> i = particles.iterator();
        while(i.hasNext()){
            PairedParticle p = i.next();
            p.update();

            if(p.isDead()){
                i.remove();
            }

        }
    }

    void draw(){
        for (PairedParticle p : particles){
            p.draw();
        }
    }

    void applyForce(PVector force){
        for (PairedParticle p : particles){
            p.applyForce(force);
        }
    }

    void addParticle(){
        PairedParticle p;
        float ran = random(4);
        float noi = noise(frameCount);
        p = new PairedParticle(origin.x, origin.y, particleMass, friction, restLen*5, k, restLen,  color(map(noi,0,1,40,200),map(noi,0,1,100,200),0));

        PVector dir = PVector.random2D();
        dir.setMag(random(2, 5));
        p.applyForce(dir);
        particles.add(p);
    }

}