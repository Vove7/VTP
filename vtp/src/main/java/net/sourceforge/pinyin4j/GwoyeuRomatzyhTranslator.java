package net.sourceforge.pinyin4j;

import net.sourceforge.pinyin4j.sparta.Document;
import net.sourceforge.pinyin4j.sparta.Element;
import net.sourceforge.pinyin4j.sparta.ParseException;

class GwoyeuRomatzyhTranslator {
   private static String[] tones = new String[]{"_I", "_II", "_III", "_IV", "_V"};

   static String convertHanyuPinyinToGwoyeuRomatzyh(String var0) {
      String var1 = TextHelper.extractPinyinString(var0);
      String var2 = TextHelper.extractToneNumber(var0);
      String var3 = null;

      try {
         String var4 = "//" + PinyinRomanizationType.HANYU_PINYIN.getTagName() + "[text()='" + var1 + "']";
         Document var5 = GwoyeuRomatzyhResource.getInstance().getPinyinToGwoyeuMappingDoc();
         Element var6 = var5.xpathSelectElement(var4);
         if (null != var6) {
            String var7 = "../" + PinyinRomanizationType.GWOYEU_ROMATZYH.getTagName() + tones[Integer.parseInt(var2) - 1] + "/text()";
            String var8 = var6.xpathSelectString(var7);
            var3 = var8;
         }
      } catch (ParseException var9) {
         var9.printStackTrace();
      }

      return var3;
   }
}
