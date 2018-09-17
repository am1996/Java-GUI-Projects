class Snake{
  public PVector pos;
  private int scl;
  private int w;
  private int h;
  public  int nTail;
  PVector[] tail = new PVector[100];
  Snake(int s,int w,int h){
    this.scl = s;
    this.w   = w;
    this.h   = h;
    this.pos = new PVector();
    this.pos.x   = (w/2)  - (this.scl);
    this.pos.y   = (h/2)  - (this.scl);
    this.nTail = 0;
  }
  public void draw(){
    this.tail[nTail] = new PVector(this.pos.x,this.pos.y);
    fill(255,255,255);
    for(int i=0;i<=this.nTail;i++){
      PVector point = this.tail[i];
      rect(point.x,point.y,this.scl,this.scl);
    }
  }
  public void move(){
    for(int i=0;i<this.nTail;i++){
      this.tail[i] = this.tail[i+1];
    }
    if(keyCode == UP) this.pos.y-=scl;
    else if(keyCode == DOWN) this.pos.y+=scl;
    else if(keyCode == LEFT) this.pos.x-=scl;
    else if(keyCode == RIGHT) this.pos.x+=scl;
    this.borderTouch();
  }
  public void borderTouch(){
    if(this.pos.x < 0)
      this.pos.x = this.w;
    else if(this.pos.x > this.w - this.scl)
      this.pos.x = 0;
    if(this.pos.y < 0)
      this.pos.y = this.h;
    else if(this.pos.y > this.h - this.scl)
      this.pos.y = 0;
  }
  public boolean eat(PVector pos){
    float dist = dist(this.pos.x,this.pos.y,pos.x,pos.y);
    if( dist < this.scl)
      return true;
    else 
      return false;
  }
  public void grow(){
    this.nTail++;
    this.tail[nTail] = new PVector(this.pos.x,this.pos.y);
  }
  public boolean isDead(){
    for(int i=0;i<nTail;i++){
      float dist = dist(this.pos.x,this.pos.y,this.tail[i].x,this.tail[i].y);
      if(dist<this.scl)
        return true;
    }
    return false;
  }
}
