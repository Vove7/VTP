package net.sourceforge.pinyin4j.sparta.xpath;

import java.io.IOException;

public class XPathException extends Exception {
   private Throwable cause_;

   public XPathException(XPath var1, String var2) {
      super(var1 + " " + var2);
      this.cause_ = null;
   }

   XPathException(XPath var1, String var2, SimpleStreamTokenizer var3, String var4) {
      this(var1, var2 + " got \"" + toString(var3) + "\" instead of expected " + var4);
   }

   XPathException(XPath var1, Exception var2) {
      super(var1 + " " + var2);
      this.cause_ = null;
      this.cause_ = var2;
   }

   public Throwable getCause() {
      return this.cause_;
   }

   private static String toString(SimpleStreamTokenizer var0) {
      try {
         StringBuffer var1 = new StringBuffer();
         var1.append(tokenToString(var0));
         if (var0.ttype != -1) {
            var0.nextToken();
            var1.append(tokenToString(var0));
            var0.pushBack();
         }

         return var1.toString();
      } catch (IOException var2) {
         return "(cannot get  info: " + var2 + ")";
      }
   }

   private static String tokenToString(SimpleStreamTokenizer var0) {
      switch(var0.ttype) {
      case -3:
         return var0.sval;
      case -2:
         return var0.nval + "";
      case -1:
         return "<end of expression>";
      default:
         return (char)var0.ttype + "";
      }
   }
}
