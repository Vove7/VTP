package net.sourceforge.pinyin4j.sparta.xpath;

public class AllElementTest extends NodeTest {
   static final AllElementTest INSTANCE = new AllElementTest();

   public void accept(Visitor var1) {
      var1.visit(this);
   }

   public boolean isStringValue() {
      return false;
   }

   public String toString() {
      return "*";
   }
}
