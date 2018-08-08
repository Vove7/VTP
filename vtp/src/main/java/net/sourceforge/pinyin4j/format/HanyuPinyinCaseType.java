package net.sourceforge.pinyin4j.format;

public class HanyuPinyinCaseType {
   public static final HanyuPinyinCaseType UPPERCASE = new HanyuPinyinCaseType("UPPERCASE");
   public static final HanyuPinyinCaseType LOWERCASE = new HanyuPinyinCaseType("LOWERCASE");
   protected String name;

   public String getName() {
      return this.name;
   }

   protected void setName(String var1) {
      this.name = var1;
   }

   protected HanyuPinyinCaseType(String var1) {
      this.setName(var1);
   }
}
