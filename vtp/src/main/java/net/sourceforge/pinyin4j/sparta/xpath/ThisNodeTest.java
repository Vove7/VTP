package net.sourceforge.pinyin4j.sparta.xpath;

public class ThisNodeTest extends NodeTest {
   static final ThisNodeTest INSTANCE = new ThisNodeTest();

   public void accept(Visitor var1) {
      var1.visit(this);
   }

   public boolean isStringValue() {
      return false;
   }

   public String toString() {
      return ".";
   }
}
