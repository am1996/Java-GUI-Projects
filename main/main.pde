int width = 640;
int height= 480;
Snake snake = new Snake(20,width,height);
Food food = new Food(20);
void setup(){
  size(640,480);
  frameRate(10);
}

void draw(){
  clear();
  background(20,20,20);
  snake.draw();
  snake.move();
  food.draw();
  if(snake.eat( food.getPosition() )){
    food.wasEaten();
    snake.grow();
  }
  if(snake.isDead())
    snake.nTail = 0;
}
