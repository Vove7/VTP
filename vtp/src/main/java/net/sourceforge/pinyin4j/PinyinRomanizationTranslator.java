package net.sourceforge.pinyin4j;

import net.sourceforge.pinyin4j.sparta.Document;
import net.sourceforge.pinyin4j.sparta.Element;
import net.sourceforge.pinyin4j.sparta.ParseException;

class PinyinRomanizationTranslator {
   static String convertRomanizationSystem(String var0, PinyinRomanizationType var1, PinyinRomanizationType var2) {
      String var3 = TextHelper.extractPinyinString(var0);
      String var4 = TextHelper.extractToneNumber(var0);
      String var5 = null;

      try {
         String var6 = "//" + var1.getTagName() + "[text()='" + var3 + "']";
         Document var7 = PinyinRomanizationResource.getInstance().getPinyinMappingDoc();
         Element var8 = var7.xpathSelectElement(var6);
         if (null != var8) {
            String var9 = "../" + var2.getTagName() + "/text()";
            String var10 = var8.xpathSelectString(var9);
            var5 = var10 + var4;
         }
      } catch (ParseException var11) {
         var11.printStackTrace();
      }

      return var5;
   }
}
