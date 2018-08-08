package net.sourceforge.pinyin4j.sparta.xpath;

import net.sourceforge.pinyin4j.sparta.Sparta;

public abstract class AttrCompareExpr extends AttrExpr {
   private final String attrValue_;

   AttrCompareExpr(String var1, String var2) {
      super(var1);
      this.attrValue_ = Sparta.intern(var2);
   }

   public String getAttrValue() {
      return this.attrValue_;
   }

   protected String toString(String var1) {
      return "[" + super.toString() + var1 + "'" + this.attrValue_ + "']";
   }
}
