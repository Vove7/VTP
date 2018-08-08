package net.sourceforge.pinyin4j.sparta.xpath;

public class PositionEqualsExpr extends BooleanExpr {
   private final int position_;

   public PositionEqualsExpr(int var1) {
      this.position_ = var1;
   }

   public void accept(BooleanExprVisitor var1) throws XPathException {
      var1.visit(this);
   }

   public int getPosition() {
      return this.position_;
   }

   public String toString() {
      return "[" + this.position_ + "]";
   }
}
