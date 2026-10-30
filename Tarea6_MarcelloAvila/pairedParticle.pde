class PairedParticle implements IAgent {

    Particle partner;
    Particle hero;
    Spring joint;
    float k;
    float restLen;
    ArrayList<IAgent> agents;

    public PairedParticle(float x, float y, float mass, float friction,
        float length, float k, float restLen, color colr) {
        agents = new ArrayList();
        mass = random(1,mass);
        hero = new Particle(x,y,mass,friction,colr,lifeSpan);
        partner = new Particle(x+length,y,mass*0.7,friction,color(colr,150),lifeSpan);
        joint = new Spring(partner, hero, k, restLen);
        agents.add(hero);
        agents.add(partner);
        agents.add(joint);
    }

    void draw(){
        partner.draw();
        joint.draw();
        hero.draw();
    }

    void update(){
        partner.update();
        joint.update();
        hero.update();
    }

    void applyForce(PVector force){
        hero.applyForce(force);
        partner.applyForce(force);
    }

    boolean isDead(){
      return hero.lifeSpan <= 0 & partner.lifeSpan <= 0;
    }
}