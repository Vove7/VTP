package net.sourceforge.pinyin4j.sparta;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class Parser {
   public static Document parse(String var0, Reader var1) throws ParseException, IOException {
      BuildDocument var2 = new BuildDocument();
      new ParseCharStream(var0, var1, (ParseLog)null, (String)null, var2);
      return var2.getDocument();
   }

   public static Document parse(String var0, Reader var1, ParseLog var2) throws ParseException, IOException {
      BuildDocument var3 = new BuildDocument();
      new ParseCharStream(var0, var1, var2, (String)null, var3);
      return var3.getDocument();
   }

   public static Document parse(String var0) throws ParseException, IOException {
      return parse(var0.toCharArray());
   }

   public static Document parse(char[] var0) throws ParseException, IOException {
      BuildDocument var1 = new BuildDocument();
      new ParseCharStream("file:anonymous-string", var0, (ParseLog)null, (String)null, var1);
      return var1.getDocument();
   }

   public static Document parse(byte[] var0) throws ParseException, IOException {
      BuildDocument var1 = new BuildDocument();
      new ParseByteStream("file:anonymous-string", new ByteArrayInputStream(var0), (ParseLog)null, (String)null, var1);
      return var1.getDocument();
   }

   public static Document parse(String var0, Reader var1, ParseLog var2, String var3) throws ParseException, EncodingMismatchException, IOException {
      BuildDocument var4 = new BuildDocument();
      new ParseCharStream(var0, var1, var2, var3, var4);
      return var4.getDocument();
   }

   public static Document parse(String var0, InputStream var1, ParseLog var2) throws ParseException, IOException {
      BuildDocument var3 = new BuildDocument();
      new ParseByteStream(var0, var1, var2, (String)null, var3);
      return var3.getDocument();
   }

   public static Document parse(String var0, InputStream var1) throws ParseException, IOException {
      BuildDocument var2 = new BuildDocument();
      new ParseByteStream(var0, var1, (ParseLog)null, (String)null, var2);
      return var2.getDocument();
   }

   public static Document parse(String var0, InputStream var1, ParseLog var2, String var3) throws ParseException, IOException {
      BuildDocument var4 = new BuildDocument();
      new ParseByteStream(var0, var1, var2, var3, var4);
      return var4.getDocument();
   }

   public static void parse(String var0, Reader var1, ParseHandler var2) throws ParseException, IOException {
      new ParseCharStream(var0, var1, (ParseLog)null, (String)null, var2);
   }

   public static void parse(String var0, Reader var1, ParseLog var2, ParseHandler var3) throws ParseException, IOException {
      new ParseCharStream(var0, var1, var2, (String)null, var3);
   }

   public static void parse(String var0, ParseHandler var1) throws ParseException, IOException {
      parse(var0.toCharArray(), var1);
   }

   public static void parse(char[] var0, ParseHandler var1) throws ParseException, IOException {
      new ParseCharStream("file:anonymous-string", var0, (ParseLog)null, (String)null, var1);
   }

   public static void parse(byte[] var0, ParseHandler var1) throws ParseException, IOException {
      new ParseByteStream("file:anonymous-string", new ByteArrayInputStream(var0), (ParseLog)null, (String)null, var1);
   }

   public static void parse(String var0, InputStream var1, ParseLog var2, ParseHandler var3) throws ParseException, IOException {
      new ParseByteStream(var0, var1, var2, (String)null, var3);
   }

   public static void parse(String var0, InputStream var1, ParseHandler var2) throws ParseException, IOException {
      new ParseByteStream(var0, var1, (ParseLog)null, (String)null, var2);
   }

   public static void parse(String var0, InputStream var1, ParseLog var2, String var3, ParseHandler var4) throws ParseException, IOException {
      new ParseByteStream(var0, var1, var2, var3, var4);
   }

   public static void parse(String var0, Reader var1, ParseLog var2, String var3, ParseHandler var4) throws ParseException, EncodingMismatchException, IOException {
      new ParseCharStream(var0, var1, var2, var3, var4);
   }
}
