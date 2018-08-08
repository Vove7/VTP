package net.sourceforge.pinyin4j.sparta;

import net.sourceforge.pinyin4j.sparta.xpath.Step;
import net.sourceforge.pinyin4j.sparta.xpath.XPath;
import net.sourceforge.pinyin4j.sparta.xpath.XPathException;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Document extends Node {
   private static final boolean DEBUG = false;
   private static final Integer ONE = new Integer(1);
   static final Enumeration EMPTY = new EmptyEnumeration();
   private Element rootElement_ = null;
   private String systemId_;
   private Sparta.Cache indices_ = Sparta.newCache();
   private Vector observers_ = new Vector();
   private final Hashtable indexible_ = (Hashtable)null;

   Document(String var1) {
      this.systemId_ = var1;
   }

   public Document() {
      this.systemId_ = "MEMORY";
   }

   public Object clone() {
      Document var1 = new Document(this.systemId_);
      var1.rootElement_ = (Element)this.rootElement_.clone();
      return var1;
   }

   public String getSystemId() {
      return this.systemId_;
   }

   public void setSystemId(String var1) {
      this.systemId_ = var1;
      this.notifyObservers();
   }

   public String toString() {
      return this.systemId_;
   }

   public Element getDocumentElement() {
      return this.rootElement_;
   }

   public void setDocumentElement(Element var1) {
      this.rootElement_ = var1;
      this.rootElement_.setOwnerDocument(this);
      this.notifyObservers();
   }

   private XPathVisitor visitor(String var1, boolean var2) throws XPathException {
      if (var1.charAt(0) != '/') {
         var1 = "/" + var1;
      }

      return this.visitor(XPath.get(var1), var2);
   }

   XPathVisitor visitor(XPath var1, boolean var2) throws XPathException {
      if (var1.isStringValue() != var2) {
         String var3 = var2 ? "evaluates to element not string" : "evaluates to string not element";
         throw new XPathException(var1, "\"" + var1 + "\" evaluates to " + var3);
      } else {
         return new XPathVisitor(this, var1);
      }
   }

   public Enumeration xpathSelectElements(String var1) throws ParseException {
      try {
         if (var1.charAt(0) != '/') {
            var1 = "/" + var1;
         }

         XPath var2 = XPath.get(var1);
         this.monitor(var2);
         return this.visitor(var2, false).getResultEnumeration();
      } catch (XPathException var3) {
         throw new ParseException("XPath problem", var3);
      }
   }

   void monitor(XPath var1) throws XPathException {
   }

   public Enumeration xpathSelectStrings(String var1) throws ParseException {
      try {
         return this.visitor(var1, true).getResultEnumeration();
      } catch (XPathException var3) {
         throw new ParseException("XPath problem", var3);
      }
   }

   public Element xpathSelectElement(String var1) throws ParseException {
      try {
         if (var1.charAt(0) != '/') {
            var1 = "/" + var1;
         }

         XPath var2 = XPath.get(var1);
         this.monitor(var2);
         return this.visitor(var2, false).getFirstResultElement();
      } catch (XPathException var3) {
         throw new ParseException("XPath problem", var3);
      }
   }

   public String xpathSelectString(String var1) throws ParseException {
      try {
         return this.visitor(var1, true).getFirstResultString();
      } catch (XPathException var3) {
         throw new ParseException("XPath problem", var3);
      }
   }

   public boolean xpathEnsure(String var1) throws ParseException {
      try {
         if (this.xpathSelectElement(var1) != null) {
            return false;
         } else {
            XPath var2 = XPath.get(var1);
            int var3 = 0;

            for(Enumeration var4 = var2.getSteps(); var4.hasMoreElements(); ++var3) {
               var4.nextElement();
            }

            Enumeration var5 = var2.getSteps();
            Step var6 = (Step)var5.nextElement();
            Step[] var7 = new Step[var3 - 1];

            for(int var8 = 0; var8 < var7.length; ++var8) {
               var7[var8] = (Step)var5.nextElement();
            }

            Element var9;
            if (this.rootElement_ == null) {
               var9 = this.makeMatching((Element)null, var6, var1);
               this.setDocumentElement(var9);
            } else {
               var9 = this.xpathSelectElement("/" + var6);
               if (var9 == null) {
                  throw new ParseException("Existing root element <" + this.rootElement_.getTagName() + "...> does not match first step \"" + var6 + "\" of \"" + var1);
               }
            }

            return var7.length == 0 ? true : this.rootElement_.xpathEnsure(XPath.get(false, var7).toString());
         }
      } catch (XPathException var10) {
         throw new ParseException(var1, var10);
      }
   }

   public boolean xpathHasIndex(String var1) {
      return this.indices_.get(var1) != null;
   }

   public Index xpathGetIndex(String var1) throws ParseException {
      try {
         Index var2 = (Index)this.indices_.get(var1);
         if (var2 == null) {
            XPath var3 = XPath.get(var1);
            var2 = new Index(var3);
            this.indices_.put(var1, var2);
         }

         return var2;
      } catch (XPathException var4) {
         throw new ParseException("XPath problem", var4);
      }
   }

   public void addObserver(Observer var1) {
      this.observers_.addElement(var1);
   }

   public void deleteObserver(Observer var1) {
      this.observers_.removeElement(var1);
   }

   public void toString(Writer var1) throws IOException {
      this.rootElement_.toString(var1);
   }

   void notifyObservers() {
      Enumeration var1 = this.observers_.elements();

      while(var1.hasMoreElements()) {
         ((Observer)var1.nextElement()).update(this);
      }

   }

   public void toXml(Writer var1) throws IOException {
      var1.write("<?xml version=\"1.0\" ?>\n");
      this.rootElement_.toXml(var1);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Document)) {
         return false;
      } else {
         Document var2 = (Document)var1;
         return this.rootElement_.equals(var2.rootElement_);
      }
   }

   protected int computeHashCode() {
      return this.rootElement_.hashCode();
   }

   public interface Observer {
      void update(Document var1);
   }

   public class Index implements Observer {
      private transient Sparta.Cache dict_ = null;
      private final XPath xpath_;
      private final String attrName_;

      Index(XPath var2) throws XPathException {
         this.attrName_ = var2.getIndexingAttrName();
         this.xpath_ = var2;
         Document.this.addObserver(this);
      }

      public synchronized Enumeration get(String var1) throws ParseException {
         if (this.dict_ == null) {
            this.regenerate();
         }

         Vector var2 = (Vector)this.dict_.get(var1);
         return var2 == null ? Document.EMPTY : var2.elements();
      }

      public synchronized int size() throws ParseException {
         if (this.dict_ == null) {
            this.regenerate();
         }

         return this.dict_.size();
      }

      public synchronized void update(Document var1) {
         this.dict_ = null;
      }

      private void regenerate() throws ParseException {
         try {
            this.dict_ = Sparta.newCache();

            Element var2;
            Vector var4;
            for(Enumeration var1 = Document.this.visitor(this.xpath_, false).getResultEnumeration(); var1.hasMoreElements(); var4.addElement(var2)) {
               var2 = (Element)var1.nextElement();
               String var3 = var2.getAttribute(this.attrName_);
               var4 = (Vector)this.dict_.get(var3);
               if (var4 == null) {
                  var4 = new Vector(1);
                  this.dict_.put(var3, var4);
               }
            }

         } catch (XPathException var5) {
            throw new ParseException("XPath problem", var5);
         }
      }
   }
}
