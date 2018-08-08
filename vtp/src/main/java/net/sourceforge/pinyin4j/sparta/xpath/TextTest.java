package net.sourceforge.pinyin4j.sparta.xpath;

public class TextTest extends NodeTest {
   static final TextTest INSTANCE = new TextTest();

   public void accept(Visitor var1) throws XPathException {
      var1.visit(this);
   }

   public boolean isStringValue() {
      return true;
   }

   public String toString() {
      return "text()";
   }
}
