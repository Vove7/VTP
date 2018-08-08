package net.sourceforge.pinyin4j.sparta;

public interface ParseSource {
   ParseLog DEFAULT_LOG = new DefaultLog();
   int MAXLOOKAHEAD = "<?xml version=\"1.0\" encoding=\"\"".length() + 40;

   String toString();

   String getSystemId();

   int getLineNumber();
}
