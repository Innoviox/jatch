package compiled;

import main.java.jatch.script.*;
import main.java.jatch.files.Reader;
import java.util.ArrayList;
import java.awt.Image;

public class Sprite1 extends Sprite {
  private String tempString;
  private boolean tempBool;
  private Controller controller;

  public Sprite1() {
    this(false);
  }

  public Sprite1(boolean cloned) {
    this.cloned = cloned;
    costumes = new ArrayList<Image>();
    costumes.add(Reader.getImageFile("forever-touching/1.svg"));
    costumes.add(Reader.getImageFile("forever-touching/2.svg"));
  }

  public void whenClicked() throws Exception {}

  public void whenAttrGreater(String attr, Object value) throws Exception {}

  public void whenBackdropSwitches(String newbn) throws Exception {}

  public void whenIRecieve(String msg) throws Exception {}

  public void whenKeyPressed(String key) throws Exception {}

  public void whenFlagClicked() throws Exception {
	System.out.println("im evil");
    while (true) {
    	  System.out.println("currently being evil");
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
