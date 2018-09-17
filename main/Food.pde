class Food{
  private int scl;
  public int x;
  public int y;
  Food(int s){
    this.scl = s;
    this.x = (int) Math.floor(Math.random() * 32) * this.scl;
    this.y = (int) Math.floor(Math.random() * 24) * this.scl;
  }
  void wasEaten(){
    this.x = (int) Math.floor(Math.random() * 32) * this.scl;
    this.y = (int) Math.floor(Math.random() * 24) * this.scl;
  }
  void draw(){
    fill(255,20,147);
    rect(this.x,this.y,this.scl,this.scl);
  }
  PVector getPosition(){
    PVector vector = new PVector(this.x,this.y);
    return vector;
  }
}
