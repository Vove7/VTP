package net.sourceforge.pinyin4j;

class PinyinRomanizationType {
   static final PinyinRomanizationType HANYU_PINYIN = new PinyinRomanizationType("Hanyu");
   static final PinyinRomanizationType WADEGILES_PINYIN = new PinyinRomanizationType("Wade");
   static final PinyinRomanizationType MPS2_PINYIN = new PinyinRomanizationType("MPSII");
   static final PinyinRomanizationType YALE_PINYIN = new PinyinRomanizationType("Yale");
   static final PinyinRomanizationType TONGYONG_PINYIN = new PinyinRomanizationType("Tongyong");
   static final PinyinRomanizationType GWOYEU_ROMATZYH = new PinyinRomanizationType("Gwoyeu");
   protected String tagName;

   protected PinyinRomanizationType(String var1) {
      this.setTagName(var1);
   }

   String getTagName() {
      return this.tagName;
   }

   protected void setTagName(String var1) {
      this.tagName = var1;
   }
}
