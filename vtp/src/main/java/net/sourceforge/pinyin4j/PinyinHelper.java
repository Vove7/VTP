package net.sourceforge.pinyin4j;

import android.content.Context;

import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinHelper {
   static Context context;

   public static void init(Context c) {
      context = c;
   }

   public static String[] toHanyuPinyinStringArray(char var0) {
      return getUnformattedHanyuPinyinStringArray(var0);
   }

   public static String[] toHanyuPinyinStringArray(char var0, HanyuPinyinOutputFormat var1) throws BadHanyuPinyinOutputFormatCombination {
      return getFormattedHanyuPinyinStringArray(var0, var1);
   }

   private static String[] getFormattedHanyuPinyinStringArray(char var0, HanyuPinyinOutputFormat var1) throws BadHanyuPinyinOutputFormatCombination {
      String[] var2 = getUnformattedHanyuPinyinStringArray(var0);
      if (null == var2) {
         return null;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = PinyinFormatter.formatHanyuPinyin(var2[var3], var1);
         }

         return var2;
      }
   }

   private static String[] getUnformattedHanyuPinyinStringArray(char var0) {
      return ChineseToPinyinResource.getInstance().getHanyuPinyinStringArray(var0);
   }

   public static String[] toTongyongPinyinStringArray(char var0) {
      return convertToTargetPinyinStringArray(var0, PinyinRomanizationType.TONGYONG_PINYIN);
   }

   public static String[] toWadeGilesPinyinStringArray(char var0) {
      return convertToTargetPinyinStringArray(var0, PinyinRomanizationType.WADEGILES_PINYIN);
   }

   public static String[] toMPS2PinyinStringArray(char var0) {
      return convertToTargetPinyinStringArray(var0, PinyinRomanizationType.MPS2_PINYIN);
   }

   public static String[] toYalePinyinStringArray(char var0) {
      return convertToTargetPinyinStringArray(var0, PinyinRomanizationType.YALE_PINYIN);
   }

   private static String[] convertToTargetPinyinStringArray(char var0, PinyinRomanizationType var1) {
      String[] var2 = getUnformattedHanyuPinyinStringArray(var0);
      if (null == var2) {
         return null;
      } else {
         String[] var3 = new String[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = PinyinRomanizationTranslator.convertRomanizationSystem(var2[var4], PinyinRomanizationType.HANYU_PINYIN, var1);
         }

         return var3;
      }
   }

   public static String[] toGwoyeuRomatzyhStringArray(char var0) {
      return convertToGwoyeuRomatzyhStringArray(var0);
   }

   private static String[] convertToGwoyeuRomatzyhStringArray(char var0) {
      String[] var1 = getUnformattedHanyuPinyinStringArray(var0);
      if (null == var1) {
         return null;
      } else {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = GwoyeuRomatzyhTranslator.convertHanyuPinyinToGwoyeuRomatzyh(var1[var3]);
         }

         return var2;
      }
   }

   /** @deprecated */
   public static String toHanyuPinyinString(String var0, HanyuPinyinOutputFormat var1, String var2) throws BadHanyuPinyinOutputFormatCombination {
      StringBuffer var3 = new StringBuffer();

      for(int var4 = 0; var4 < var0.length(); ++var4) {
         String var5 = getFirstHanyuPinyinString(var0.charAt(var4), var1);
         if (null != var5) {
            var3.append(var5);
            if (var4 != var0.length() - 1) {
               var3.append(var2);
            }
         } else {
            var3.append(var0.charAt(var4));
         }
      }

      return var3.toString();
   }

   /** @deprecated */
   private static String getFirstHanyuPinyinString(char var0, HanyuPinyinOutputFormat var1) throws BadHanyuPinyinOutputFormatCombination {
      String[] var2 = getFormattedHanyuPinyinStringArray(var0, var1);
      return null != var2 && var2.length > 0 ? var2[0] : null;
   }
}
