package net.sourceforge.pinyin4j.sparta;

public interface ParseLog {
   void error(String var1, String var2, int var3);

   void warning(String var1, String var2, int var3);

   void note(String var1, String var2, int var3);
}
