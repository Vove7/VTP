package net.sourceforge.pinyin4j.sparta;

class BuildDocument implements DocumentSource, ParseHandler {
   private final ParseLog log_;
   private Element currentElement_;
   private final Document doc_;
   private ParseSource parseSource_;

   public BuildDocument() {
      this((ParseLog)null);
   }

   public BuildDocument(ParseLog var1) {
      this.currentElement_ = null;
      this.doc_ = new Document();
      this.parseSource_ = null;
      this.log_ = var1 == null ? ParseSource.DEFAULT_LOG : var1;
   }

   public void setParseSource(ParseSource var1) {
      this.parseSource_ = var1;
      this.doc_.setSystemId(var1.toString());
   }

   public ParseSource getParseSource() {
      return this.parseSource_;
   }

   public String toString() {
      return this.parseSource_ != null ? "BuildDoc: " + this.parseSource_.toString() : null;
   }

   public String getSystemId() {
      return this.parseSource_ != null ? this.parseSource_.getSystemId() : null;
   }

   public int getLineNumber() {
      return this.parseSource_ != null ? this.parseSource_.getLineNumber() : -1;
   }

   public Document getDocument() {
      return this.doc_;
   }

   public void startDocument() {
   }

   public void endDocument() {
   }

   public void startElement(Element var1) {
      if (this.currentElement_ == null) {
         this.doc_.setDocumentElement(var1);
      } else {
         this.currentElement_.appendChild(var1);
      }

      this.currentElement_ = var1;
   }

   public void endElement(Element var1) {
      this.currentElement_ = this.currentElement_.getParentNode();
   }

   public void characters(char[] var1, int var2, int var3) {
      Element var4 = this.currentElement_;
      Text var5;
      if (var4.getLastChild() instanceof Text) {
         var5 = (Text)var4.getLastChild();
         var5.appendData(var1, var2, var3);
      } else {
         var5 = new Text(new String(var1, var2, var3));
         var4.appendChildNoChecking(var5);
      }

   }
}
