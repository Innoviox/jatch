package compiled;

import main.java.jatch.script.*;

public class Sprite1 extends Sprite {
  private String tempString;
  private boolean tempBool;

  public void whenClicked() throws Exception {}

  public void whenAttrGreater(String attr, Object value) throws Exception {}

  public void whenBackdropSwitches(String newbn) throws Exception {}

  public void whenIRecieve(String msg) throws Exception {}

  public void whenKeyPressed(String key) throws Exception {}

  public void whenFlagClicked() throws Exception {
    while (true) {
      tempString = "_mouse_";
      if (tempString.equals("_mouse_")) {
        tempBool = touchingPtr();
      } else {
        tempBool = touching(tempString);
      }
      ;
      if (tempBool) {
        turnR(15);
      } else {
        turnL(15);
      }
    }
  }

  public void whenCloned() throws Exception {}
}
