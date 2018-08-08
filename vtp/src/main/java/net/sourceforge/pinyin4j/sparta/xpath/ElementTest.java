package net.sourceforge.pinyin4j.sparta.xpath;

import net.sourceforge.pinyin4j.sparta.Sparta;

public class ElementTest extends NodeTest {
   private final String tagName_;

   ElementTest(String var1) {
      this.tagName_ = Sparta.intern(var1);
   }

   public void accept(Visitor var1) {
      var1.visit(this);
   }

   public boolean isStringValue() {
      return false;
   }

   public String getTagName() {
      return this.tagName_;
   }

   public String toString() {
      return this.tagName_;
   }
}
