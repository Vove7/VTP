package net.sourceforge.pinyin4j;

class TextHelper {
   static String extractToneNumber(String var0) {
      return var0.substring(var0.length() - 1);
   }

   static String extractPinyinString(String var0) {
      return var0.substring(0, var0.length() - 1);
   }
}
