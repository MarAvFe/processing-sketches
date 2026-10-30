
       boolean flag = true;

void setup() {
       size(400, 400);
       stroke(255);
     }
      
     void draw() {
       line(150, 25, mouseX, mouseY);
     }
     
     void mousePressed() {
       if (flag){
         background(192, 64, 0);
       }else{
         background(0,128,128);
       }
         flag = !flag;
     }