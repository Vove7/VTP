package net.sourceforge.pinyin4j.sparta.xpath;

public abstract class TextCompareExpr extends BooleanExpr {
   private final String value_;

   TextCompareExpr(String var1) {
      this.value_ = var1;
   }

   public String getValue() {
      return this.value_;
   }

   protected String toString(String var1) {
      return "[text()" + var1 + "'" + this.value_ + "']";
   }
}
