package net.sourceforge.pinyin4j.sparta.xpath;

public class TextExistsExpr extends BooleanExpr {
   static final TextExistsExpr INSTANCE = new TextExistsExpr();

   public void accept(BooleanExprVisitor var1) throws XPathException {
      var1.visit(this);
   }

   public String toString() {
      return "[text()]";
   }
}
