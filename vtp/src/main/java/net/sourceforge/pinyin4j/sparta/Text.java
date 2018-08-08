package net.sourceforge.pinyin4j.sparta;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

public class Text extends Node {
   private StringBuffer text_;

   public Text(String var1) {
      this.text_ = new StringBuffer(var1);
   }

   public Text(char var1) {
      this.text_ = new StringBuffer();
      this.text_.append(var1);
   }

   public Object clone() {
      return new Text(this.text_.toString());
   }

   public void appendData(String var1) {
      this.text_.append(var1);
      this.notifyObservers();
   }

   public void appendData(char var1) {
      this.text_.append(var1);
      this.notifyObservers();
   }

   public void appendData(char[] var1, int var2, int var3) {
      this.text_.append(var1, var2, var3);
      this.notifyObservers();
   }

   public String getData() {
      return this.text_.toString();
   }

   public void setData(String var1) {
      this.text_ = new StringBuffer(var1);
      this.notifyObservers();
   }

   void toXml(Writer var1) throws IOException {
      String var2 = this.text_.toString();
      if (var2.length() < 50) {
         Node.htmlEncode(var1, var2);
      } else {
         var1.write("<![CDATA[");
         var1.write(var2);
         var1.write("]]>");
      }

   }

   void toString(Writer var1) throws IOException {
      var1.write(this.text_.toString());
   }

   public Enumeration xpathSelectElements(String var1) {
      throw new Error("Sorry, not implemented");
   }

   public Enumeration xpathSelectStrings(String var1) {
      throw new Error("Sorry, not implemented");
   }

   public Element xpathSelectElement(String var1) {
      throw new Error("Sorry, not implemented");
   }

   public String xpathSelectString(String var1) {
      throw new Error("Sorry, not implemented");
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Text)) {
         return false;
      } else {
         Text var2 = (Text)var1;
         return this.text_.toString().equals(var2.text_.toString());
      }
   }

   protected int computeHashCode() {
      return this.text_.toString().hashCode();
   }
}
