package net.sourceforge.pinyin4j.sparta;

public interface ParseHandler {
   void setParseSource(ParseSource var1);

   ParseSource getParseSource();

   void startDocument() throws ParseException;

   void endDocument() throws ParseException;

   void startElement(Element var1) throws ParseException;

   void endElement(Element var1) throws ParseException;

   void characters(char[] var1, int var2, int var3) throws ParseException;
}
