package compiled;

import main.java.jatch.script.*;
import main.java.jatch.files.Reader;
import java.util.ArrayList;
import java.awt.Image;

public class Sprite1 extends Sprite {
  private String tempString;
  private boolean tempBool;
  private Controller controller;
  private ExprEval ee;

  public Sprite1() {
    this(false);
  }

  public Sprite1(boolean cloned) {
    this.cloned = cloned;
    this.addMouseListener(this);
    costumes = new ArrayList<Image>();
    costumes.add(Reader.getImageFile("google-interview-project/1.svg"));
    costumes.add(Reader.getImageFile("google-interview-project/2.svg"));
  }

  public void whenClicked() throws Exception {}

  public void whenAttrGreater(String attr, Object value) throws Exception {}

  public void whenBackdropSwitches(String newbn) throws Exception {}

  public void whenIRecieve(String msg) throws Exception {}

  public void whenKeyPressed(String key) throws Exception {}

  public void whenFlagClicked() throws Exception {
    goTo(0, 0);
    for (int i = 0; i < 10; i++) {
      move(10);
      turnR(15);
      Thread.sleep(1000 / 40);
    }
    for (int i = 0; i < 10; i++) {
      move(10);
      turnL(15);
      Thread.sleep(1000 / 40);
    }
    ee = new ExprEval("[&, [|, [<, 2, 3], [>, 5, 6]], [=, [+, 2, 3], [-, 6, 1]]]", this);
    tempBool = Boolean.parseBoolean(ee.parse());
    if (tempBool) {
      glide(1, 0, 0);
    }
  }

  public void whenCloned() throws Exception {}
}
