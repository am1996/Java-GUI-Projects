class Snake{
  public int x;
  public int y;
  private int scl;
  private int w;
  private int h;
  Snake(int s,int w,int h){
    this.scl = s;
    this.w   = w;
    this.h   = h;
    this.x   = (w/2)  - (this.scl/2);
    this.y   = (h/2)  - (this.scl/2);
  }
  public void draw(){
    fill(255,255,255);
    rect(x,y,scl,scl);
  }
  public void move(){
    if(keyCode == UP) this.y-=scl;
    else if(keyCode == DOWN) this.y+=scl;
    else if(keyCode == LEFT) this.x-=scl;
    else if(keyCode == RIGHT) this.x+=scl;
    this.x   = constrain(this.x,0, w - scl);
    this.y   = constrain(this.y,0, h - scl);
  }
  public boolean gameOver(){
    if(this.x == 0 || this.y == 0 || this.x > this.w-this.scl)
      return true;
    else return false;
  }
}
