package net.sourceforge.pinyin4j.sparta.xpath;

public class TextNotEqualsExpr extends TextCompareExpr {
   TextNotEqualsExpr(String var1) {
      super(var1);
   }

   public void accept(BooleanExprVisitor var1) throws XPathException {
      var1.visit(this);
   }

   public String toString() {
      return this.toString("!=");
   }
}
