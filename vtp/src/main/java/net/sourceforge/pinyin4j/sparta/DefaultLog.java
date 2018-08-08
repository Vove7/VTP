package net.sourceforge.pinyin4j.sparta;

class DefaultLog implements ParseLog {
   public void error(String var1, String var2, int var3) {
      System.err.println(var2 + "(" + var3 + "): " + var1 + " (ERROR)");
   }

   public void warning(String var1, String var2, int var3) {
      System.out.println(var2 + "(" + var3 + "): " + var1 + " (WARNING)");
   }

   public void note(String var1, String var2, int var3) {
      System.out.println(var2 + "(" + var3 + "): " + var1 + " (NOTE)");
   }
}
