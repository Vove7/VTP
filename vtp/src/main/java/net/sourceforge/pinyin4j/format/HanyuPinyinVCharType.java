package net.sourceforge.pinyin4j.format;

public class HanyuPinyinVCharType {
   public static final HanyuPinyinVCharType WITH_U_AND_COLON = new HanyuPinyinVCharType("WITH_U_AND_COLON");
   public static final HanyuPinyinVCharType WITH_V = new HanyuPinyinVCharType("WITH_V");
   public static final HanyuPinyinVCharType WITH_U_UNICODE = new HanyuPinyinVCharType("WITH_U_UNICODE");
   protected String name;

   public String getName() {
      return this.name;
   }

   protected void setName(String var1) {
      this.name = var1;
   }

   protected HanyuPinyinVCharType(String var1) {
      this.setName(var1);
   }
}
