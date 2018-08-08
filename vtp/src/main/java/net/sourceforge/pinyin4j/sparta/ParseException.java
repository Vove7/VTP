package net.sourceforge.pinyin4j.sparta;

public class ParseException extends Exception {
   private int lineNumber_;
   private Throwable cause_;

   public ParseException(String var1) {
      super(var1);
      this.lineNumber_ = -1;
      this.cause_ = null;
   }

   public ParseException(String var1, Throwable var2) {
      super(var1 + " " + var2);
      this.lineNumber_ = -1;
      this.cause_ = null;
      this.cause_ = var2;
   }

   public ParseException(String var1, int var2, int var3, String var4, String var5) {
      super(toMessage(var1, var2, var3, var4, var5));
      this.lineNumber_ = -1;
      this.cause_ = null;
      this.lineNumber_ = var2;
   }

   public ParseException(ParseLog var1, String var2, int var3, int var4, String var5, String var6) {
      this(var2, var3, var4, var5, var6);
      var1.error(var6, var2, var3);
   }

   public ParseException(ParseCharStream var1, String var2) {
      this(var1.getLog(), var1.getSystemId(), var1.getLineNumber(), var1.getLastCharRead(), var1.getHistory(), var2);
   }

   public ParseException(ParseCharStream var1, char var2, char var3) {
      this(var1, "got '" + var2 + "' instead of expected '" + var3 + "'");
   }

   public ParseException(ParseCharStream var1, char var2, char[] var3) {
      this(var1, "got '" + var2 + "' instead of " + toString(var3));
   }

   public ParseException(ParseCharStream var1, char var2, String var3) {
      this(var1, "got '" + var2 + "' instead of " + var3 + " as expected");
   }

   public ParseException(ParseCharStream var1, String var2, String var3) {
      this(var1, "got \"" + var2 + "\" instead of \"" + var3 + "\" as expected");
   }

   private static String toString(char[] var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append(var0[0]);

      for(int var2 = 1; var2 < var0.length; ++var2) {
         var1.append("or " + var0[var2]);
      }

      return var1.toString();
   }

   public ParseException(ParseCharStream var1, String var2, char[] var3) {
      this(var1, var2, new String(var3));
   }

   public int getLineNumber() {
      return this.lineNumber_;
   }

   public Throwable getCause() {
      return this.cause_;
   }

   private static String toMessage(String var0, int var1, int var2, String var3, String var4) {
      return var0 + "(" + var1 + "): \n" + var3 + "\nLast character read was '" + charRepr(var2) + "'\n" + var4;
   }

   static String charRepr(int var0) {
      return var0 == -1 ? "EOF" : "" + (char)var0;
   }
}
