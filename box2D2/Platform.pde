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
