package compiled;

import main.java.jatch.script.*;
import main.java.jatch.files.Reader;
import java.util.ArrayList;
import java.awt.Image;

public class Apple extends Sprite {
  private String tempString;
  private boolean tempBool;
  private Controller controller;
  private ExprEval ee;

  public Apple() {
    this(false);
  }

  public Apple(boolean cloned) {
    this.cloned = cloned;
    costumes = new ArrayList<Image>();
    costumes.add(Reader.getImageFile("forever-touching-multisprite-rotation/3.svg"));
  }

  public void whenClicked() throws Exception {}

  public void whenAttrGreater(String attr, Object value) throws Exception {}

  public void whenBackdropSwitches(String newbn) throws Exception {}

  public void whenIRecieve(String msg) throws Exception {}

  public void whenKeyPressed(String key) throws Exception {}

  public void whenFlagClicked() throws Exception {
    while (!tempBool) {
      tempString = "_mouse_";
      if (tempString.equals("_mouse_")) {
        tempBool = touchingPtr();
      } else {
        tempBool = touching(tempString);
      }
      ;
      if (tempBool) {
        move(10);
      }
      turnR(15);
      ee = new ExprEval("[=, 1, 2]", this);
      tempBool = Boolean.parseBoolean(ee.parse());
      Thread.sleep(1000 / 40);
    }
  }

  public void whenCloned() throws Exception {}
}
