package net.sourceforge.pinyin4j.sparta.xpath;

public class AttrGreaterExpr extends AttrRelationalExpr {
   public AttrGreaterExpr(String var1, int var2) {
      super(var1, var2);
   }

   public void accept(BooleanExprVisitor var1) throws XPathException {
      var1.visit(this);
   }

   public String toString() {
      return this.toString(">");
   }
}
