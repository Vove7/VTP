package net.sourceforge.pinyin4j.sparta;

import net.sourceforge.pinyin4j.sparta.xpath.Step;
import net.sourceforge.pinyin4j.sparta.xpath.XPath;
import net.sourceforge.pinyin4j.sparta.xpath.XPathException;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Element extends Node {
   private static final boolean DEBUG = false;
   private Node firstChild_ = null;
   private Node lastChild_ = null;
   private Hashtable attributes_ = null;
   private Vector attributeNames_ = null;
   private String tagName_ = null;

   public Element(String var1) {
      this.tagName_ = Sparta.intern(var1);
   }

   Element() {
   }

   public Object clone() {
      return this.cloneElement(true);
   }

   public Element cloneShallow() {
      return this.cloneElement(false);
   }

   public Element cloneElement(boolean var1) {
      Element var2 = new Element(this.tagName_);
      if (this.attributeNames_ != null) {
         Enumeration var3 = this.attributeNames_.elements();

         while(var3.hasMoreElements()) {
            String var4 = (String)var3.nextElement();
            var2.setAttribute(var4, (String)this.attributes_.get(var4));
         }
      }

      if (var1) {
         for(Node var5 = this.firstChild_; var5 != null; var5 = var5.getNextSibling()) {
            var2.appendChild((Node)var5.clone());
         }
      }

      return var2;
   }

   public String getTagName() {
      return this.tagName_;
   }

   public void setTagName(String var1) {
      this.tagName_ = Sparta.intern(var1);
      this.notifyObservers();
   }

   public Node getFirstChild() {
      return this.firstChild_;
   }

   public Node getLastChild() {
      return this.lastChild_;
   }

   public Enumeration getAttributeNames() {
      return this.attributeNames_ == null ? Document.EMPTY : this.attributeNames_.elements();
   }

   public String getAttribute(String var1) {
      return this.attributes_ == null ? null : (String)this.attributes_.get(var1);
   }

   public void setAttribute(String var1, String var2) {
      if (this.attributes_ == null) {
         this.attributes_ = new Hashtable();
         this.attributeNames_ = new Vector();
      }

      if (this.attributes_.get(var1) == null) {
         this.attributeNames_.addElement(var1);
      }

      this.attributes_.put(var1, var2);
      this.notifyObservers();
   }

   public void removeAttribute(String var1) {
      if (this.attributes_ != null) {
         this.attributes_.remove(var1);
         this.attributeNames_.removeElement(var1);
         this.notifyObservers();
      }
   }

   void appendChildNoChecking(Node var1) {
      Element var2 = var1.getParentNode();
      if (var2 != null) {
         var2.removeChildNoChecking(var1);
      }

      var1.insertAtEndOfLinkedList(this.lastChild_);
      if (this.firstChild_ == null) {
         this.firstChild_ = var1;
      }

      var1.setParentNode(this);
      this.lastChild_ = var1;
      var1.setOwnerDocument(this.getOwnerDocument());
   }

   public void appendChild(Node var1) {
      if (!this.canHaveAsDescendent((Node)var1)) {
         var1 = (Element)((Node)var1).clone();
      }

      this.appendChildNoChecking((Node)var1);
      this.notifyObservers();
   }

   boolean canHaveAsDescendent(Node var1) {
      if (var1 == this) {
         return false;
      } else {
         Element var2 = this.getParentNode();
         return var2 == null ? true : var2.canHaveAsDescendent(var1);
      }
   }

   private boolean removeChildNoChecking(Node var1) {
      int var2 = 0;

      for(Node var3 = this.firstChild_; var3 != null; var3 = var3.getNextSibling()) {
         if (var3.equals(var1)) {
            if (this.firstChild_ == var3) {
               this.firstChild_ = var3.getNextSibling();
            }

            if (this.lastChild_ == var3) {
               this.lastChild_ = var3.getPreviousSibling();
            }

            var3.removeFromLinkedList();
            var3.setParentNode((Element)null);
            var3.setOwnerDocument((Document)null);
            return true;
         }

         ++var2;
      }

      return false;
   }

   public void removeChild(Node var1) throws DOMException {
      boolean var2 = this.removeChildNoChecking(var1);
      if (!var2) {
         throw new DOMException((short)8, "Cannot find " + var1 + " in " + this);
      } else {
         this.notifyObservers();
      }
   }

   public void replaceChild(Element var1, Node var2) throws DOMException {
      this.replaceChild_(var1, var2);
      this.notifyObservers();
   }

   public void replaceChild(Text var1, Node var2) throws DOMException {
      this.replaceChild_(var1, var2);
      this.notifyObservers();
   }

   private void replaceChild_(Node var1, Node var2) throws DOMException {
      int var3 = 0;

      for(Node var4 = this.firstChild_; var4 != null; var4 = var4.getNextSibling()) {
         if (var4 == var2) {
            if (this.firstChild_ == var2) {
               this.firstChild_ = var1;
            }

            if (this.lastChild_ == var2) {
               this.lastChild_ = var1;
            }

            var2.replaceInLinkedList(var1);
            var1.setParentNode(this);
            var2.setParentNode((Element)null);
            return;
         }

         ++var3;
      }

      throw new DOMException((short)8, "Cannot find " + var2 + " in " + this);
   }

   void toString(Writer var1) throws IOException {
      for(Node var2 = this.firstChild_; var2 != null; var2 = var2.getNextSibling()) {
         var2.toString(var1);
      }

   }

   public void toXml(Writer var1) throws IOException {
      var1.write("<" + this.tagName_);
      if (this.attributeNames_ != null) {
         Enumeration var2 = this.attributeNames_.elements();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            String var4 = (String)this.attributes_.get(var3);
            var1.write(" " + var3 + "=\"");
            Node.htmlEncode(var1, var4);
            var1.write("\"");
         }
      }

      if (this.firstChild_ == null) {
         var1.write("/>");
      } else {
         var1.write(">");

         for(Node var5 = this.firstChild_; var5 != null; var5 = var5.getNextSibling()) {
            var5.toXml(var1);
         }

         var1.write("</" + this.tagName_ + ">");
      }

   }

   private XPathVisitor visitor(String var1, boolean var2) throws XPathException {
      XPath var3 = XPath.get(var1);
      if (var3.isStringValue() != var2) {
         String var4 = var2 ? "evaluates to element not string" : "evaluates to string not element";
         throw new XPathException(var3, "\"" + var3 + "\" evaluates to " + var4);
      } else {
         return new XPathVisitor(this, var3);
      }
   }

   public Enumeration xpathSelectElements(String var1) throws ParseException {
      try {
         return this.visitor(var1, false).getResultEnumeration();
      } catch (XPathException var3) {
         throw new ParseException("XPath problem", var3);
      }
   }

   public Enumeration xpathSelectStrings(String var1) throws ParseException {
      try {
         return this.visitor(var1, true).getResultEnumeration();
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

            Step[] var5 = new Step[var3 - 1];
            Enumeration var6 = var2.getSteps();

            for(int var7 = 0; var7 < var5.length; ++var7) {
               var5[var7] = (Step)var6.nextElement();
            }

            Step var8 = (Step)var6.nextElement();
            Element var9;
            if (var5.length == 0) {
               var9 = this;
            } else {
               String var10 = XPath.get(var2.isAbsolute(), var5).toString();
               this.xpathEnsure(var10.toString());
               var9 = this.xpathSelectElement(var10);
            }

            Element var12 = this.makeMatching(var9, var8, var1);
            var9.appendChildNoChecking(var12);
            return true;
         }
      } catch (XPathException var11) {
         throw new ParseException(var1, var11);
      }
   }

   public Element xpathSelectElement(String var1) throws ParseException {
      try {
         return this.visitor(var1, false).getFirstResultElement();
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

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Element)) {
         return false;
      } else {
         Element var2 = (Element)var1;
         if (!this.tagName_.equals(var2.tagName_)) {
            return false;
         } else {
            int var3 = this.attributes_ == null ? 0 : this.attributes_.size();
            int var4 = var2.attributes_ == null ? 0 : var2.attributes_.size();
            if (var3 != var4) {
               return false;
            } else {
               if (this.attributes_ != null) {
                  Enumeration var5 = this.attributes_.keys();

                  while(var5.hasMoreElements()) {
                     String var6 = (String)var5.nextElement();
                     String var7 = (String)this.attributes_.get(var6);
                     String var8 = (String)var2.attributes_.get(var6);
                     if (!var7.equals(var8)) {
                        return false;
                     }
                  }
               }

               Node var9 = this.firstChild_;

               for(Node var10 = var2.firstChild_; var9 != null; var10 = var10.getNextSibling()) {
                  if (!var9.equals(var10)) {
                     return false;
                  }

                  var9 = var9.getNextSibling();
               }

               return true;
            }
         }
      }
   }

   protected int computeHashCode() {
      int var1 = this.tagName_.hashCode();
      String var4;
      if (this.attributes_ != null) {
         for(Enumeration var2 = this.attributes_.keys(); var2.hasMoreElements(); var1 = 31 * var1 + var4.hashCode()) {
            String var3 = (String)var2.nextElement();
            var1 = 31 * var1 + var3.hashCode();
            var4 = (String)this.attributes_.get(var3);
         }
      }

      for(Node var5 = this.firstChild_; var5 != null; var5 = var5.getNextSibling()) {
         var1 = 31 * var1 + var5.hashCode();
      }

      return var1;
   }

   private void checkInvariant() {
   }
}
