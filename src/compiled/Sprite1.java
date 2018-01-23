package compiled;

import main.java.jatch.script.*;

public class Sprite1 extends Sprite {
  private String tempString;
  private boolean tempBool;
  private Controller controller;

  public Sprite1() {
    this(false);
  }

  public Sprite1(boolean cloned) {
    this.cloned = cloned;
  }

  private Object private1 = 0;

  public void whenClicked() throws Exception {}

  public void whenAttrGreater(String attr, Object value) throws Exception {}

  public void whenBackdropSwitches(String newbn) throws Exception {}

  public void whenIRecieve(String msg) throws Exception {}

  public void whenKeyPressed(String key) throws Exception {}

  public void whenFlagClicked() throws Exception {
    set("private1", 0);
    set("global", 0);
    change("private1", 1);
    change("global", 1);
    showVar("private1");
    showVar("global");
    hideVar("global");
    hideVar("private1");
  }

  public void whenCloned() throws Exception {}
}
