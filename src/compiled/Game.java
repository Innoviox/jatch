package compiled;

import main.java.jatch.script.*;
import main.java.jatch.graphics.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
  public static void main(String[] args) throws Exception {
    List<Sprite> sprites = new ArrayList<Sprite>();
    sprites.add(new Sprite1());
    Stage stage = new Stage("google-interview-project/3.png");
    Controller c = new GameController(sprites, stage);
    DrawController dc = new DrawController(c);
    dc.start();
  }
}
