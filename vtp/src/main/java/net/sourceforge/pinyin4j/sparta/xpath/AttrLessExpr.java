package net.sourceforge.pinyin4j.sparta.xpath;

public class AttrLessExpr extends AttrRelationalExpr {
   public AttrLessExpr(String var1, int var2) {
      super(var1, var2);
   }

   public void accept(BooleanExprVisitor var1) throws XPathException {
      var1.visit(this);
   }

   public String toString() {
      return this.toString("<");
   }
}
