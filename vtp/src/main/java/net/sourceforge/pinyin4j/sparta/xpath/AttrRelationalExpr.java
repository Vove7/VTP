package net.sourceforge.pinyin4j.sparta.xpath;

public abstract class AttrRelationalExpr extends AttrExpr {
   private final int attrValue_;

   AttrRelationalExpr(String var1, int var2) {
      super(var1);
      this.attrValue_ = var2;
   }

   public double getAttrValue() {
      return (double)this.attrValue_;
   }

   protected String toString(String var1) {
      return "[" + super.toString() + var1 + "'" + this.attrValue_ + "']";
   }
}
