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
    while (!tempBool) {
      ExprEval ee = new ExprEval("[=, [readVariable, i], 3]", this);
      tempBool = Boolean.parseBoolean(ee.parse());
      change("i", 1);
    }
  }

  public void whenCloned() {}
}
