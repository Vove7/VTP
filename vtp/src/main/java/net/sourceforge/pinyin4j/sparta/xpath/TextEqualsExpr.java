package net.sourceforge.pinyin4j.sparta.xpath;

public class TextEqualsExpr extends TextCompareExpr {
   TextEqualsExpr(String var1) {
      super(var1);
   }

   public void accept(BooleanExprVisitor var1) throws XPathException {
      var1.visit(this);
   }

   public String toString() {
      return this.toString("=");
   }
}
