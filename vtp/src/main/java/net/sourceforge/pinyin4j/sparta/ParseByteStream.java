package net.sourceforge.pinyin4j.sparta;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

class ParseByteStream implements ParseSource {
   private ParseCharStream parseSource_;

   public ParseByteStream(String var1, InputStream var2, ParseLog var3, String var4, ParseHandler var5) throws ParseException, IOException {
      if (var3 == null) {
         var3 = ParseSource.DEFAULT_LOG;
      }

      if (!var2.markSupported()) {
         throw new Error("Precondition violation: the InputStream passed to ParseByteStream must support mark");
      } else {
         var2.mark(ParseSource.MAXLOOKAHEAD);
         byte[] var6 = new byte[4];
         int var7 = var2.read(var6);
         if (var4 == null) {
            var4 = guessEncoding(var1, var6, var7, var3);
         }

         try {
            var2.reset();
            InputStreamReader var8 = new InputStreamReader(var2, fixEncoding(var4));

            try {
               this.parseSource_ = new ParseCharStream(var1, var8, var3, var4, var5);
            } catch (IOException var14) {
               String var16 = "euc-jp";
               var3.note("Problem reading with assumed encoding of " + var4 + " so restarting with " + var16, var1, 1);
               var2.reset();

               try {
                  var8 = new InputStreamReader(var2, fixEncoding(var16));
               } catch (UnsupportedEncodingException var13) {
                  throw new ParseException(var3, var1, 1, 0, var16, "\"" + var16 + "\" is not a supported encoding");
               }

               this.parseSource_ = new ParseCharStream(var1, var8, var3, (String)null, var5);
            }
         } catch (EncodingMismatchException var15) {
            String var9 = var15.getDeclaredEncoding();
            var3.note("Encoding declaration of " + var9 + " is different that assumed " + var4 + " so restarting the parsing with the new encoding", var1, 1);
            var2.reset();

            InputStreamReader var10;
            try {
               var10 = new InputStreamReader(var2, fixEncoding(var9));
            } catch (UnsupportedEncodingException var12) {
               throw new ParseException(var3, var1, 1, 0, var9, "\"" + var9 + "\" is not a supported encoding");
            }

            this.parseSource_ = new ParseCharStream(var1, var10, var3, (String)null, var5);
         }

      }
   }

   public String toString() {
      return this.parseSource_.toString();
   }

   public String getSystemId() {
      return this.parseSource_.getSystemId();
   }

   public int getLineNumber() {
      return this.parseSource_.getLineNumber();
   }

   private static String guessEncoding(String var0, byte[] var1, int var2, ParseLog var3) throws IOException {
      String var4;
      if (var2 != 4) {
         String var5 = var2 <= 0 ? "no characters in input" : "less than 4 characters in input: \"" + new String(var1, 0, var2) + "\"";
         var3.error(var5, var0, 1);
         var4 = "UTF-8";
      } else if (!equals(var1, (int)65279) && !equals(var1, -131072) && !equals(var1, (int)65534) && !equals(var1, -16842752) && !equals(var1, (int)60) && !equals(var1, 1006632960) && !equals(var1, (int)15360) && !equals(var1, 3932160)) {
         if (equals(var1, 3932223)) {
            var4 = "UTF-16BE";
         } else if (equals(var1, 1006649088)) {
            var4 = "UTF-16LE";
         } else if (equals(var1, 1010792557)) {
            var4 = "UTF-8";
         } else if (equals(var1, 1282385812)) {
            var4 = "EBCDIC";
         } else if (!equals(var1, (short)-2) && !equals(var1, (short)-257)) {
            var4 = "UTF-8";
         } else {
            var4 = "UTF-16";
         }
      } else {
         var4 = "UCS-4";
      }

      if (!var4.equals("UTF-8")) {
         var3.note("From start " + hex(var1[0]) + " " + hex(var1[1]) + " " + hex(var1[2]) + " " + hex(var1[3]) + " deduced encoding = " + var4, var0, 1);
      }

      return var4;
   }

   private static String hex(byte var0) {
      String var1 = Integer.toHexString(var0);
      switch(var1.length()) {
      case 1:
         return "0" + var1;
      case 2:
         return var1;
      default:
         return var1.substring(var1.length() - 2);
      }
   }

   private static boolean equals(byte[] var0, int var1) {
      return var0[0] == (byte)(var1 >>> 24) && var0[1] == (byte)(var1 >>> 16 & 255) && var0[2] == (byte)(var1 >>> 8 & 255) && var0[3] == (byte)(var1 & 255);
   }

   private static boolean equals(byte[] var0, short var1) {
      return var0[0] == (byte)(var1 >>> 8) && var0[1] == (byte)(var1 & 255);
   }

   private static String fixEncoding(String var0) {
      return var0.toLowerCase().equals("utf8") ? "UTF-8" : var0;
   }
}
