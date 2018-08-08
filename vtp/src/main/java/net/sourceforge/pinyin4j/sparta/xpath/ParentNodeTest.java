package net.sourceforge.pinyin4j.sparta.xpath;

public class ParentNodeTest extends NodeTest {
   static final ParentNodeTest INSTANCE = new ParentNodeTest();

   public void accept(Visitor var1) throws XPathException {
      var1.visit(this);
   }

   public boolean isStringValue() {
      return false;
   }

   public String toString() {
      return "..";
   }
}
