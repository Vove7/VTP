package net.sourceforge.pinyin4j.sparta.xpath;

public class TrueExpr extends BooleanExpr {
   static final TrueExpr INSTANCE = new TrueExpr();

   public void accept(BooleanExprVisitor var1) {
      var1.visit(this);
   }

   public String toString() {
      return "";
   }
}
