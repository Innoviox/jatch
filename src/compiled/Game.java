package compiled;

import main.java.jatch.script.*;
import main.java.jatch.graphics.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
  public static void main(String[] args) throws Exception {
    List<Sprite> sprites = new ArrayList<Sprite>();
    sprites.add(new Sprite1());
    sprites.add(new Apple());
    Stage stage = new Stage("forever-touching-multisprite-rotation/4.svg");
    Controller c = new GameController(sprites, stage);
    DrawController dc = new DrawController(c);
    dc.start();
  }
}
