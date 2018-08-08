package net.sourceforge.pinyin4j.sparta;

public class EncodingMismatchException extends ParseException {
   private String declaredEncoding_;

   EncodingMismatchException(String var1, String var2, String var3) {
      super(var1, 0, var2.charAt(var2.length() - 1), var2, "encoding '" + var2 + "' declared instead of of " + var3 + " as expected");
      this.declaredEncoding_ = var2;
   }

   String getDeclaredEncoding() {
      return this.declaredEncoding_;
   }
}
