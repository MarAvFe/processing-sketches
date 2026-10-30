/*
 Simulación de Sistemas Naturales
 Marcello Ávila
 2014084118

 Tarea 7 - Mouse Seeking

 Descripción:
    Se crea un flow field con ruido perlin.
    Con el click del mouse se crean vehículos con dirección aleatoria
        en la posición del mouse. Su color también será aleatorio,
        basado en ruido perlin.
    Cada vehículo cuenta con una masa de entre [1-5] aleatoriamente. Según
        el valor obtenido se mapean su tamaño de [5-15], su velocidad
        máxima de [5-2] de tal manera que los más grandes sean más lentos.
        Y su fuerza máxima de [0.04-0.1] como el ejemplo planteaba. Además
        su radio de llegada será de [50,200].
    La zona blanca alrededor del mouse, dibuja el radio máximo de llegada
        de 200px.
    Se puede dibujar el rastro de los vehículos, pero es una característica
        pesada. Se recomienda activarla solo con el flow field apagado y pocos
        vehículos en pantalla.

Acciones:
    s : (s)how flow field
    m : show (m)ouse radius
    r : (r)eset vehicles to zero
    t : (t)rail on/off
*/

FlowField field;
ArrayList<Vehicle> vehicles;
float defaultMag;
boolean showField;
boolean showMouse;
boolean showTrail;
int RES = 25;

void setup(){
    size(800,600,FX2D);
    defaultMag = 1;
    field = new FlowField(RES,defaultMag);
    vehicles = new ArrayList();
    showField = true;
    showMouse = true;
    showTrail = false;
    background(0);
}
void draw(){
    if(showTrail){
        fill(0,30);
        noStroke();
        rect(0,0,width,height);
    }else{
        background(0);
    }
    if(showField) field.display();
    field.update();
    PVector mouse = new PVector(mouseX, mouseY);
    stroke(255);
    fill(255,10);
    if(showMouse) ellipse(mouse.x,mouse.y,200,200);
    for(Vehicle v : vehicles){
        PVector mouseSeek = PVector.sub(mouse,v.pos);
        mouseSeek.setMag(defaultMag);
        v.applyForce(mouseSeek);
        PVector f = field.getForce(v.pos.x, v.pos.y);
        v.applyForce(f);
        v.update();
        v.display();
    }
    if(mousePressed){
        float r = map(noise(frameCount*0.01,1),0,1,0,255);
        float g = map(noise(frameCount*0.01,4),0,1,0,255);
        float b = map(noise(frameCount*0.01,7),0,1,0,255);
        colorMode(HSB);
        vehicles.add(new Vehicle(mouseX, mouseY, PVector.random2D(), color(r,g,b,80)));
    }
    if(keyPressed && (key == 'r' || key == 'R')) vehicles.clear();
}

void keyPressed(){
    if(key == 's' || key == 'S') showField = !showField;
    if(key == 'm' || key == 'M') showMouse = !showMouse;
    if(key == 't' || key == 'T') showTrail = !showTrail;
}
