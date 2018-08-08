package net.sourceforge.pinyin4j.sparta.xpath;

public class AttrTest extends NodeTest {
   private final String attrName_;

   AttrTest(String var1) {
      this.attrName_ = var1;
   }

   public void accept(Visitor var1) {
      var1.visit(this);
   }

   public boolean isStringValue() {
      return true;
   }

   public String getAttrName() {
      return this.attrName_;
   }

   public String toString() {
      return "@" + this.attrName_;
   }
}
