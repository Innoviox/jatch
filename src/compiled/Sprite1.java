package compiled;

import main.java.jatch.script.*;

public class Sprite1 extends Sprite {
  private String tempString;
  private boolean tempBool;

  public void whenClicked() {}

  public void whenAttrGreater(String attr, Object value) {}

  public void whenBackdropSwitches(String newbn) {}

  public void whenIRecieve(String msg) {}

  public void whenKeyPressed(String key) {}

  public void whenFlagClicked() {
    for (int i = 0; i < 10; i++) {
      ExprEval ee = new ExprEval("[=, [+, 1, 2], 3]");
      tempBool = Boolean.parseBoolean(ee.parse());
      if (tempBool) {
        turnR(15);
      } else {
        turnL(15);
      }
    }
  }

  public void whenCloned() {}
}
