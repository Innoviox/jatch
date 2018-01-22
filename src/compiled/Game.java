package compiled;

import main.java.jatch.script.*;
import main.java.jatch.graphics.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
  public static void main(String[] args) throws Exception {
    List<Sprite> sprites = new ArrayList<Sprite>();
    sprites.add(new Sprite1());
    sprites.add(new Sprite2());
    Stage stage = new Stage("backdrop1");
    Controller c = new GameController(sprites, stage);
    DrawController dc = new DrawController(c);
    dc.start();
  }
}
