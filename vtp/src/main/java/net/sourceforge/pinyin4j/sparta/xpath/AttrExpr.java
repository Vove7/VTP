package net.sourceforge.pinyin4j.sparta.xpath;

public abstract class AttrExpr extends BooleanExpr {
   private final String attrName_;

   AttrExpr(String var1) {
      this.attrName_ = var1;
   }

   public String getAttrName() {
      return this.attrName_;
   }

   public String toString() {
      return "@" + this.attrName_;
   }
}
