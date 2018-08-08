package net.sourceforge.pinyin4j.sparta;

import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

class ParseCharStream implements ParseSource {
   private static final boolean DEBUG = true;
   private static final boolean H_DEBUG = false;
   private static final char[] NAME_PUNCT_CHARS = new char[]{'.', '-', '_', ':'};
   private static final int MAX_COMMON_CHAR = 128;
   private static final boolean[] IS_NAME_CHAR = new boolean[128];
   private static final char[] COMMENT_BEGIN;
   private static final char[] COMMENT_END;
   private static final char[] PI_BEGIN;
   private static final char[] QU_END;
   private static final char[] DOCTYPE_BEGIN;
   private static final char[] XML_BEGIN;
   private static final char[] ENCODING;
   private static final char[] VERSION;
   private static final char[] VERSIONNUM_PUNC_CHARS;
   private static final char[] MARKUPDECL_BEGIN;
   private static final char[] CHARREF_BEGIN;
   private static final char[] ENTITY_BEGIN;
   private static final char[] NDATA;
   private static final char[] SYSTEM;
   private static final char[] PUBLIC;
   private static final char[] BEGIN_CDATA;
   private static final char[] END_CDATA;
   private static final char[] END_EMPTYTAG;
   private static final char[] BEGIN_ETAG;
   private String systemId_;
   private String docTypeName_;
   private final Reader reader_;
   private final Hashtable entities_;
   private final Hashtable pes_;
   private final ParseLog log_;
   private final String encoding_;
   private int ch_;
   private boolean isExternalDtd_;
   private final int CBUF_SIZE;
   private final char[] cbuf_;
   private int curPos_;
   private int endPos_;
   private boolean eos_;
   private static final int TMP_BUF_SIZE = 255;
   private final char[] tmpBuf_;
   private int lineNumber_;
   private final CharCircBuffer history_;
   public static final int HISTORY_LENGTH = 100;
   private final ParseHandler handler_;

   public ParseCharStream(String var1, char[] var2, ParseLog var3, String var4, ParseHandler var5) throws ParseException, EncodingMismatchException, IOException {
      this(var1, (Reader)null, var2, var3, var4, var5);
   }

   public ParseCharStream(String var1, Reader var2, ParseLog var3, String var4, ParseHandler var5) throws ParseException, EncodingMismatchException, IOException {
      this(var1, var2, (char[])null, var3, var4, var5);
   }

   public ParseCharStream(String var1, Reader var2, char[] var3, ParseLog var4, String var5, ParseHandler var6) throws ParseException, EncodingMismatchException, IOException {
      this.docTypeName_ = null;
      this.entities_ = new Hashtable();
      this.pes_ = new Hashtable();
      this.ch_ = -2;
      this.isExternalDtd_ = false;
      this.CBUF_SIZE = 1024;
      this.curPos_ = 0;
      this.endPos_ = 0;
      this.eos_ = false;
      this.tmpBuf_ = new char[255];
      this.lineNumber_ = -1;
      this.lineNumber_ = 1;
      this.history_ = null;
      this.log_ = var4 == null ? ParseSource.DEFAULT_LOG : var4;
      this.encoding_ = var5 == null ? null : var5.toLowerCase();
      this.entities_.put("lt", "<");
      this.entities_.put("gt", ">");
      this.entities_.put("amp", "&");
      this.entities_.put("apos", "'");
      this.entities_.put("quot", "\"");
      if (var3 != null) {
         this.cbuf_ = var3;
         this.curPos_ = 0;
         this.endPos_ = this.cbuf_.length;
         this.eos_ = true;
         this.reader_ = null;
      } else {
         this.reader_ = var2;
         this.cbuf_ = new char[1024];
         this.fillBuf();
      }

      this.systemId_ = var1;
      this.handler_ = var6;
      this.handler_.setParseSource(this);
      this.readProlog();
      this.handler_.startDocument();
      Element var7 = this.readElement();
      if (this.docTypeName_ != null && !this.docTypeName_.equals(var7.getTagName())) {
         this.log_.warning("DOCTYPE name \"" + this.docTypeName_ + "\" not same as tag name, \"" + var7.getTagName() + "\" of root element", this.systemId_, this.getLineNumber());
      }

      while(this.isMisc()) {
         this.readMisc();
      }

      if (this.reader_ != null) {
         this.reader_.close();
      }

      this.handler_.endDocument();
   }

   public String toString() {
      return this.systemId_;
   }

   public String getSystemId() {
      return this.systemId_;
   }

   public int getLineNumber() {
      return this.lineNumber_;
   }

   int getLastCharRead() {
      return this.ch_;
   }

   final String getHistory() {
      return "";
   }

   private int fillBuf() throws IOException {
      if (this.eos_) {
         return -1;
      } else {
         if (this.endPos_ == this.cbuf_.length) {
            this.curPos_ = this.endPos_ = 0;
         }

         int var1 = this.reader_.read(this.cbuf_, this.endPos_, this.cbuf_.length - this.endPos_);
         if (var1 <= 0) {
            this.eos_ = true;
            return -1;
         } else {
            this.endPos_ += var1;
            return var1;
         }
      }
   }

   private int fillBuf(int var1) throws IOException {
      if (this.eos_) {
         return -1;
      } else {
         int var2 = 0;
         int var3;
         if (this.cbuf_.length - this.curPos_ < var1) {
            for(var3 = 0; this.curPos_ + var3 < this.endPos_; ++var3) {
               this.cbuf_[var3] = this.cbuf_[this.curPos_ + var3];
            }

            var2 = this.endPos_ - this.curPos_;
            this.endPos_ = var2;
            this.curPos_ = 0;
         }

         var3 = this.fillBuf();
         if (var3 == -1) {
            return var2 == 0 ? -1 : var2;
         } else {
            return var2 + var3;
         }
      }
   }

   private final char readChar() throws ParseException, IOException {
      if (this.curPos_ >= this.endPos_ && this.fillBuf() == -1) {
         throw new ParseException(this, "unexpected end of expression.");
      } else {
         if (this.cbuf_[this.curPos_] == '\n') {
            ++this.lineNumber_;
         }

         return this.cbuf_[this.curPos_++];
      }
   }

   private final char peekChar() throws ParseException, IOException {
      if (this.curPos_ >= this.endPos_ && this.fillBuf() == -1) {
         throw new ParseException(this, "unexpected end of expression.");
      } else {
         return this.cbuf_[this.curPos_];
      }
   }

   private final void readChar(char var1) throws ParseException, IOException {
      char var2 = this.readChar();
      if (var2 != var1) {
         throw new ParseException(this, var2, var1);
      }
   }

   private final boolean isChar(char var1) throws ParseException, IOException {
      if (this.curPos_ >= this.endPos_ && this.fillBuf() == -1) {
         throw new ParseException(this, "unexpected end of expression.");
      } else {
         return this.cbuf_[this.curPos_] == var1;
      }
   }

   private final char readChar(char var1, char var2) throws ParseException, IOException {
      char var3 = this.readChar();
      if (var3 != var1 && var3 != var2) {
         throw new ParseException(this, var3, new char[]{var1, var2});
      } else {
         return var3;
      }
   }

   private final char readChar(char var1, char var2, char var3, char var4) throws ParseException, IOException {
      char var5 = this.readChar();
      if (var5 != var1 && var5 != var2 && var5 != var3 && var5 != var4) {
         throw new ParseException(this, var5, new char[]{var1, var2, var3, var4});
      } else {
         return var5;
      }
   }

   private final boolean isChar(char var1, char var2) throws ParseException, IOException {
      if (this.curPos_ >= this.endPos_ && this.fillBuf() == -1) {
         return false;
      } else {
         char var3 = this.cbuf_[this.curPos_];
         return var3 == var1 || var3 == var2;
      }
   }

   private final boolean isChar(char var1, char var2, char var3, char var4) throws ParseException, IOException {
      if (this.curPos_ >= this.endPos_ && this.fillBuf() == -1) {
         return false;
      } else {
         char var5 = this.cbuf_[this.curPos_];
         return var5 == var1 || var5 == var2 || var5 == var3 || var5 == var4;
      }
   }

   private static final boolean isIn(char var0, char[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var0 == var1[var2]) {
            return true;
         }
      }

      return false;
   }

   private final void readS() throws ParseException, IOException {
      this.readChar(' ', '\t', '\r', '\n');

      while(this.isChar(' ', '\t', '\r', '\n')) {
         this.readChar();
      }

   }

   private final boolean isS() throws ParseException, IOException {
      return this.isChar(' ', '\t', '\r', '\n');
   }

   private boolean isNameChar() throws ParseException, IOException {
      char var1 = this.peekChar();
      return var1 < 128 ? IS_NAME_CHAR[var1] : isNameChar(var1);
   }

   private static boolean isLetter(char var0) {
      return "abcdefghijklmnopqrstuvwxyz".indexOf(Character.toLowerCase(var0)) != -1;
   }

   private static boolean isNameChar(char var0) {
      return Character.isDigit(var0) || isLetter(var0) || isIn(var0, NAME_PUNCT_CHARS) || isExtender(var0);
   }

   private static boolean isExtender(char var0) {
      switch(var0) {
      case '·':
      case 'ː':
      case 'ˑ':
      case '·':
      case 'ـ':
      case 'ๆ':
      case 'ໆ':
      case '々':
      case '〱':
      case '〲':
      case '〳':
      case '〴':
      case '〵':
      case 'ゝ':
      case 'ゞ':
      case 'ー':
      case 'ヽ':
      case 'ヾ':
         return true;
      default:
         return false;
      }
   }

   private final String readName() throws ParseException, IOException {
      StringBuffer var1 = null;
      byte var2 = 0;
      int var3 = var2 + 1;

      for(this.tmpBuf_[var2] = this.readNameStartChar(); this.isNameChar(); this.tmpBuf_[var3++] = this.readChar()) {
         if (var3 >= 255) {
            if (var1 == null) {
               var1 = new StringBuffer(var3);
               var1.append(this.tmpBuf_, 0, var3);
            } else {
               var1.append(this.tmpBuf_, 0, var3);
            }

            var3 = 0;
         }
      }

      if (var1 == null) {
         return Sparta.intern(new String(this.tmpBuf_, 0, var3));
      } else {
         var1.append(this.tmpBuf_, 0, var3);
         return var1.toString();
      }
   }

   private char readNameStartChar() throws ParseException, IOException {
      char var1 = this.readChar();
      if (!isLetter(var1) && var1 != '_' && var1 != ':') {
         throw new ParseException(this, var1, "letter, underscore, colon");
      } else {
         return var1;
      }
   }

   private final String readEntityValue() throws ParseException, IOException {
      char var1 = this.readChar('\'', '"');
      StringBuffer var2 = new StringBuffer();

      while(!this.isChar(var1)) {
         if (this.isPeReference()) {
            var2.append(this.readPeReference());
         } else if (this.isReference()) {
            var2.append(this.readReference());
         } else {
            var2.append(this.readChar());
         }
      }

      this.readChar(var1);
      return var2.toString();
   }

   private final boolean isEntityValue() throws ParseException, IOException {
      return this.isChar('\'', '"');
   }

   private final void readSystemLiteral() throws ParseException, IOException {
      char var1 = this.readChar();

      while(this.peekChar() != var1) {
         this.readChar();
      }

      this.readChar(var1);
   }

   private final void readPubidLiteral() throws ParseException, IOException {
      this.readSystemLiteral();
   }

   private boolean isMisc() throws ParseException, IOException {
      return this.isComment() || this.isPi() || this.isS();
   }

   private void readMisc() throws ParseException, IOException {
      if (this.isComment()) {
         this.readComment();
      } else if (this.isPi()) {
         this.readPi();
      } else {
         if (!this.isS()) {
            throw new ParseException(this, "expecting comment or processing instruction or space");
         }

         this.readS();
      }

   }

   private final void readComment() throws ParseException, IOException {
      this.readSymbol(COMMENT_BEGIN);

      while(!this.isSymbol(COMMENT_END)) {
         this.readChar();
      }

      this.readSymbol(COMMENT_END);
   }

   private final boolean isComment() throws ParseException, IOException {
      return this.isSymbol(COMMENT_BEGIN);
   }

   private final void readPi() throws ParseException, IOException {
      this.readSymbol(PI_BEGIN);

      while(!this.isSymbol(QU_END)) {
         this.readChar();
      }

      this.readSymbol(QU_END);
   }

   private final boolean isPi() throws ParseException, IOException {
      return this.isSymbol(PI_BEGIN);
   }

   private void readProlog() throws ParseException, EncodingMismatchException, IOException {
      if (this.isXmlDecl()) {
         this.readXmlDecl();
      }

      while(this.isMisc()) {
         this.readMisc();
      }

      if (this.isDocTypeDecl()) {
         this.readDocTypeDecl();

         while(this.isMisc()) {
            this.readMisc();
         }
      }

   }

   private boolean isDocTypeDecl() throws ParseException, IOException {
      return this.isSymbol(DOCTYPE_BEGIN);
   }

   private void readXmlDecl() throws ParseException, EncodingMismatchException, IOException {
      this.readSymbol(XML_BEGIN);
      this.readVersionInfo();
      if (this.isS()) {
         this.readS();
      }

      if (this.isEncodingDecl()) {
         String var1 = this.readEncodingDecl();
         if (this.encoding_ != null && !var1.toLowerCase().equals(this.encoding_)) {
            throw new EncodingMismatchException(this.systemId_, var1, this.encoding_);
         }
      }

      while(!this.isSymbol(QU_END)) {
         this.readChar();
      }

      this.readSymbol(QU_END);
   }

   private boolean isXmlDecl() throws ParseException, IOException {
      return this.isSymbol(XML_BEGIN);
   }

   private boolean isEncodingDecl() throws ParseException, IOException {
      return this.isSymbol(ENCODING);
   }

   private String readEncodingDecl() throws ParseException, IOException {
      this.readSymbol(ENCODING);
      this.readEq();
      char var1 = this.readChar('\'', '"');
      StringBuffer var2 = new StringBuffer();

      while(!this.isChar(var1)) {
         var2.append(this.readChar());
      }

      this.readChar(var1);
      return var2.toString();
   }

   private void readVersionInfo() throws ParseException, IOException {
      this.readS();
      this.readSymbol(VERSION);
      this.readEq();
      char var1 = this.readChar('\'', '"');
      this.readVersionNum();
      this.readChar(var1);
   }

   private final void readEq() throws ParseException, IOException {
      if (this.isS()) {
         this.readS();
      }

      this.readChar('=');
      if (this.isS()) {
         this.readS();
      }

   }

   private boolean isVersionNumChar() throws ParseException, IOException {
      char var1 = this.peekChar();
      return Character.isDigit(var1) || 'a' <= var1 && var1 <= 'z' || 'Z' <= var1 && var1 <= 'Z' || isIn(var1, VERSIONNUM_PUNC_CHARS);
   }

   private void readVersionNum() throws ParseException, IOException {
      this.readChar();

      while(this.isVersionNumChar()) {
         this.readChar();
      }

   }

   private void readDocTypeDecl() throws ParseException, IOException {
      this.readSymbol(DOCTYPE_BEGIN);
      this.readS();
      this.docTypeName_ = this.readName();
      if (this.isS()) {
         this.readS();
         if (!this.isChar('>') && !this.isChar('[')) {
            this.isExternalDtd_ = true;
            this.readExternalId();
            if (this.isS()) {
               this.readS();
            }
         }
      }

      if (this.isChar('[')) {
         this.readChar();

         while(!this.isChar(']')) {
            if (this.isDeclSep()) {
               this.readDeclSep();
            } else {
               this.readMarkupDecl();
            }
         }

         this.readChar(']');
         if (this.isS()) {
            this.readS();
         }
      }

      this.readChar('>');
   }

   private void readDeclSep() throws ParseException, IOException {
      if (this.isPeReference()) {
         this.readPeReference();
      } else {
         this.readS();
      }

   }

   private boolean isDeclSep() throws ParseException, IOException {
      return this.isPeReference() || this.isS();
   }

   private void readMarkupDecl() throws ParseException, IOException {
      if (this.isPi()) {
         this.readPi();
      } else if (this.isComment()) {
         this.readComment();
      } else if (this.isEntityDecl()) {
         this.readEntityDecl();
      } else {
         if (!this.isSymbol(MARKUPDECL_BEGIN)) {
            throw new ParseException(this, "expecting processing instruction, comment, or \"<!\"");
         }

         while(true) {
            while(!this.isChar('>')) {
               if (this.isChar('\'', '"')) {
                  char var1 = this.readChar();

                  while(!this.isChar(var1)) {
                     this.readChar();
                  }

                  this.readChar(var1);
               } else {
                  this.readChar();
               }
            }

            this.readChar('>');
            break;
         }
      }

   }

   private char readCharRef() throws ParseException, IOException {
      this.readSymbol(CHARREF_BEGIN);
      byte var1 = 10;
      if (this.isChar('x')) {
         this.readChar();
         var1 = 16;
      }

      int var2 = 0;

      while(!this.isChar(';')) {
         this.tmpBuf_[var2++] = this.readChar();
         if (var2 >= 255) {
            this.log_.warning("Tmp buffer overflow on readCharRef", this.systemId_, this.getLineNumber());
            return ' ';
         }
      }

      this.readChar(';');
      String var3 = new String(this.tmpBuf_, 0, var2);

      try {
         return (char)Integer.parseInt(var3, var1);
      } catch (NumberFormatException var5) {
         this.log_.warning("\"" + var3 + "\" is not a valid " + (var1 == 16 ? "hexadecimal" : "decimal") + " number", this.systemId_, this.getLineNumber());
         return ' ';
      }
   }

   private final char[] readReference() throws ParseException, IOException {
      return this.isSymbol(CHARREF_BEGIN) ? new char[]{this.readCharRef()} : this.readEntityRef().toCharArray();
   }

   private final boolean isReference() throws ParseException, IOException {
      return this.isChar('&');
   }

   private String readEntityRef() throws ParseException, IOException {
      this.readChar('&');
      String var1 = this.readName();
      String var2 = (String)this.entities_.get(var1);
      if (var2 == null) {
         var2 = "";
         if (this.isExternalDtd_) {
            this.log_.warning("&" + var1 + "; not found -- possibly defined in external DTD)", this.systemId_, this.getLineNumber());
         } else {
            this.log_.warning("No declaration of &" + var1 + ";", this.systemId_, this.getLineNumber());
         }
      }

      this.readChar(';');
      return var2;
   }

   private String readPeReference() throws ParseException, IOException {
      this.readChar('%');
      String var1 = this.readName();
      String var2 = (String)this.pes_.get(var1);
      if (var2 == null) {
         var2 = "";
         this.log_.warning("No declaration of %" + var1 + ";", this.systemId_, this.getLineNumber());
      }

      this.readChar(';');
      return var2;
   }

   private boolean isPeReference() throws ParseException, IOException {
      return this.isChar('%');
   }

   private void readEntityDecl() throws ParseException, IOException {
      this.readSymbol(ENTITY_BEGIN);
      this.readS();
      String var1;
      String var2;
      if (this.isChar('%')) {
         this.readChar('%');
         this.readS();
         var1 = this.readName();
         this.readS();
         if (this.isEntityValue()) {
            var2 = this.readEntityValue();
         } else {
            var2 = this.readExternalId();
         }

         this.pes_.put(var1, var2);
      } else {
         var1 = this.readName();
         this.readS();
         if (this.isEntityValue()) {
            var2 = this.readEntityValue();
         } else {
            if (!this.isExternalId()) {
               throw new ParseException(this, "expecting double-quote, \"PUBLIC\" or \"SYSTEM\" while reading entity declaration");
            }

            var2 = this.readExternalId();
            if (this.isS()) {
               this.readS();
            }

            if (this.isSymbol(NDATA)) {
               this.readSymbol(NDATA);
               this.readS();
               this.readName();
            }
         }

         this.entities_.put(var1, var2);
      }

      if (this.isS()) {
         this.readS();
      }

      this.readChar('>');
   }

   private boolean isEntityDecl() throws ParseException, IOException {
      return this.isSymbol(ENTITY_BEGIN);
   }

   private String readExternalId() throws ParseException, IOException {
      if (this.isSymbol(SYSTEM)) {
         this.readSymbol(SYSTEM);
      } else {
         if (!this.isSymbol(PUBLIC)) {
            throw new ParseException(this, "expecting \"SYSTEM\" or \"PUBLIC\" while reading external ID");
         }

         this.readSymbol(PUBLIC);
         this.readS();
         this.readPubidLiteral();
      }

      this.readS();
      this.readSystemLiteral();
      return "(WARNING: external ID not read)";
   }

   private boolean isExternalId() throws ParseException, IOException {
      return this.isSymbol(SYSTEM) || this.isSymbol(PUBLIC);
   }

   private final void readSymbol(char[] var1) throws ParseException, IOException {
      int var2 = var1.length;
      if (this.endPos_ - this.curPos_ < var2 && this.fillBuf(var2) <= 0) {
         this.ch_ = -1;
         throw new ParseException(this, "end of XML file", var1);
      } else {
         this.ch_ = this.cbuf_[this.endPos_ - 1];
         if (this.endPos_ - this.curPos_ < var2) {
            throw new ParseException(this, "end of XML file", var1);
         } else {
            for(int var3 = 0; var3 < var2; ++var3) {
               if (this.cbuf_[this.curPos_ + var3] != var1[var3]) {
                  throw new ParseException(this, new String(this.cbuf_, this.curPos_, var2), var1);
               }
            }

            this.curPos_ += var2;
         }
      }
   }

   private final boolean isSymbol(char[] var1) throws ParseException, IOException {
      int var2 = var1.length;
      if (this.endPos_ - this.curPos_ < var2 && this.fillBuf(var2) <= 0) {
         this.ch_ = -1;
         return false;
      } else {
         this.ch_ = this.cbuf_[this.endPos_ - 1];
         if (this.endPos_ - this.curPos_ < var2) {
            return false;
         } else {
            for(int var3 = 0; var3 < var2; ++var3) {
               if (this.cbuf_[this.curPos_ + var3] != var1[var3]) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private String readAttValue() throws ParseException, IOException {
      char var1 = this.readChar('\'', '"');
      StringBuffer var2 = new StringBuffer();

      while(!this.isChar(var1)) {
         if (this.isReference()) {
            var2.append(this.readReference());
         } else {
            var2.append(this.readChar());
         }
      }

      this.readChar(var1);
      return var2.toString();
   }

   private void readPossibleCharData() throws ParseException, IOException {
      int var1 = 0;

      while(!this.isChar('<') && !this.isChar('&') && !this.isSymbol(END_CDATA)) {
         this.tmpBuf_[var1] = this.readChar();
         if (this.tmpBuf_[var1] == '\r' && this.peekChar() == '\n') {
            this.tmpBuf_[var1] = this.readChar();
         }

         ++var1;
         if (var1 == 255) {
            this.handler_.characters(this.tmpBuf_, 0, 255);
            var1 = 0;
         }
      }

      if (var1 > 0) {
         this.handler_.characters(this.tmpBuf_, 0, var1);
      }

   }

   private void readCdSect() throws ParseException, IOException {
      StringBuffer var1 = null;
      this.readSymbol(BEGIN_CDATA);

      int var2;
      for(var2 = 0; !this.isSymbol(END_CDATA); this.tmpBuf_[var2++] = this.readChar()) {
         if (var2 >= 255) {
            if (var1 == null) {
               var1 = new StringBuffer(var2);
               var1.append(this.tmpBuf_, 0, var2);
            } else {
               var1.append(this.tmpBuf_, 0, var2);
            }

            var2 = 0;
         }
      }

      this.readSymbol(END_CDATA);
      if (var1 != null) {
         var1.append(this.tmpBuf_, 0, var2);
         char[] var3 = var1.toString().toCharArray();
         this.handler_.characters(var3, 0, var3.length);
      } else {
         this.handler_.characters(this.tmpBuf_, 0, var2);
      }

   }

   private boolean isCdSect() throws ParseException, IOException {
      return this.isSymbol(BEGIN_CDATA);
   }

   private final Element readElement() throws ParseException, IOException {
      Element var1 = new Element();
      boolean var2 = this.readEmptyElementTagOrSTag(var1);
      this.handler_.startElement(var1);
      if (var2) {
         this.readContent();
         this.readETag(var1);
      }

      this.handler_.endElement(var1);
      return var1;
   }

   ParseLog getLog() {
      return this.log_;
   }

   private boolean readEmptyElementTagOrSTag(Element var1) throws ParseException, IOException {
      this.readChar('<');
      var1.setTagName(this.readName());

      while(this.isS()) {
         this.readS();
         if (!this.isChar('/', '>')) {
            this.readAttribute(var1);
         }
      }

      if (this.isS()) {
         this.readS();
      }

      boolean var2 = this.isChar('>');
      if (var2) {
         this.readChar('>');
      } else {
         this.readSymbol(END_EMPTYTAG);
      }

      return var2;
   }

   private void readAttribute(Element var1) throws ParseException, IOException {
      String var2 = this.readName();
      this.readEq();
      String var3 = this.readAttValue();
      if (var1.getAttribute(var2) != null) {
         this.log_.warning("Element " + this + " contains attribute " + var2 + "more than once", this.systemId_, this.getLineNumber());
      }

      var1.setAttribute(var2, var3);
   }

   private void readETag(Element var1) throws ParseException, IOException {
      this.readSymbol(BEGIN_ETAG);
      String var2 = this.readName();
      if (!var2.equals(var1.getTagName())) {
         this.log_.warning("end tag (" + var2 + ") does not match begin tag (" + var1.getTagName() + ")", this.systemId_, this.getLineNumber());
      }

      if (this.isS()) {
         this.readS();
      }

      this.readChar('>');
   }

   private boolean isETag() throws ParseException, IOException {
      return this.isSymbol(BEGIN_ETAG);
   }

   private void readContent() throws ParseException, IOException {
      this.readPossibleCharData();

      for(boolean var1 = true; var1; this.readPossibleCharData()) {
         if (this.isETag()) {
            var1 = false;
         } else if (this.isReference()) {
            char[] var2 = this.readReference();
            this.handler_.characters(var2, 0, var2.length);
         } else if (this.isCdSect()) {
            this.readCdSect();
         } else if (this.isPi()) {
            this.readPi();
         } else if (this.isComment()) {
            this.readComment();
         } else if (this.isChar('<')) {
            this.readElement();
         } else {
            var1 = false;
         }
      }

   }

   static {
      for(char var0 = 0; var0 < 128; ++var0) {
         IS_NAME_CHAR[var0] = isNameChar(var0);
      }

      COMMENT_BEGIN = "<!--".toCharArray();
      COMMENT_END = "-->".toCharArray();
      PI_BEGIN = "<?".toCharArray();
      QU_END = "?>".toCharArray();
      DOCTYPE_BEGIN = "<!DOCTYPE".toCharArray();
      XML_BEGIN = "<?xml".toCharArray();
      ENCODING = "encoding".toCharArray();
      VERSION = "version".toCharArray();
      VERSIONNUM_PUNC_CHARS = new char[]{'_', '.', ':', '-'};
      MARKUPDECL_BEGIN = "<!".toCharArray();
      CHARREF_BEGIN = "&#".toCharArray();
      ENTITY_BEGIN = "<!ENTITY".toCharArray();
      NDATA = "NDATA".toCharArray();
      SYSTEM = "SYSTEM".toCharArray();
      PUBLIC = "PUBLIC".toCharArray();
      BEGIN_CDATA = "<![CDATA[".toCharArray();
      END_CDATA = "]]>".toCharArray();
      END_EMPTYTAG = "/>".toCharArray();
      BEGIN_ETAG = "</".toCharArray();
   }
}
