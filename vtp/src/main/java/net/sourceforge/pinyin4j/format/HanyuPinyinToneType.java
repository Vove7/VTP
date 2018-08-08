package net.sourceforge.pinyin4j.format;

public class HanyuPinyinToneType {
   public static final HanyuPinyinToneType WITH_TONE_NUMBER = new HanyuPinyinToneType("WITH_TONE_NUMBER");
   public static final HanyuPinyinToneType WITHOUT_TONE = new HanyuPinyinToneType("WITHOUT_TONE");
   public static final HanyuPinyinToneType WITH_TONE_MARK = new HanyuPinyinToneType("WITH_TONE_MARK");
   protected String name;

   public String getName() {
      return this.name;
   }

   protected void setName(String var1) {
      this.name = var1;
   }

   protected HanyuPinyinToneType(String var1) {
      this.setName(var1);
   }
}
