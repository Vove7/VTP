package net.sourceforge.pinyin4j.sparta;

import net.sourceforge.pinyin4j.sparta.xpath.AttrEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.AttrExistsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.AttrGreaterExpr;
import net.sourceforge.pinyin4j.sparta.xpath.AttrLessExpr;
import net.sourceforge.pinyin4j.sparta.xpath.AttrNotEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.BooleanExpr;
import net.sourceforge.pinyin4j.sparta.xpath.BooleanExprVisitor;
import net.sourceforge.pinyin4j.sparta.xpath.ElementTest;
import net.sourceforge.pinyin4j.sparta.xpath.NodeTest;
import net.sourceforge.pinyin4j.sparta.xpath.PositionEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.Step;
import net.sourceforge.pinyin4j.sparta.xpath.TextEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.TextExistsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.TextNotEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.TrueExpr;
import net.sourceforge.pinyin4j.sparta.xpath.XPath;
import net.sourceforge.pinyin4j.sparta.xpath.XPathException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

public abstract class Node {
   private Document doc_ = null;
   private Element parentNode_ = null;
   private Node previousSibling_ = null;
   private Node nextSibling_ = null;
   private Object annotation_ = null;
   private int hash_ = 0;

   void notifyObservers() {
      this.hash_ = 0;
      if (this.doc_ != null) {
         this.doc_.notifyObservers();
      }

   }

   void setOwnerDocument(Document var1) {
      this.doc_ = var1;
   }

   public Document getOwnerDocument() {
      return this.doc_;
   }

   public Element getParentNode() {
      return this.parentNode_;
   }

   public Node getPreviousSibling() {
      return this.previousSibling_;
   }

   public Node getNextSibling() {
      return this.nextSibling_;
   }

   public Object getAnnotation() {
      return this.annotation_;
   }

   public void setAnnotation(Object var1) {
      this.annotation_ = var1;
   }

   void setParentNode(Element var1) {
      this.parentNode_ = var1;
   }

   void insertAtEndOfLinkedList(Node var1) {
      this.previousSibling_ = var1;
      if (var1 != null) {
         var1.nextSibling_ = this;
      }

   }

   void removeFromLinkedList() {
      if (this.previousSibling_ != null) {
         this.previousSibling_.nextSibling_ = this.nextSibling_;
      }

      if (this.nextSibling_ != null) {
         this.nextSibling_.previousSibling_ = this.previousSibling_;
      }

      this.nextSibling_ = null;
      this.previousSibling_ = null;
   }

   void replaceInLinkedList(Node var1) {
      if (this.previousSibling_ != null) {
         this.previousSibling_.nextSibling_ = var1;
      }

      if (this.nextSibling_ != null) {
         this.nextSibling_.previousSibling_ = var1;
      }

      var1.nextSibling_ = this.nextSibling_;
      var1.previousSibling_ = this.previousSibling_;
      this.nextSibling_ = null;
      this.previousSibling_ = null;
   }

   public String toXml() throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      OutputStreamWriter var2 = new OutputStreamWriter(var1);
      this.toXml(var2);
      var2.flush();
      return new String(var1.toByteArray());
   }

   public boolean xpathSetStrings(String var1, String var2) throws ParseException {
      boolean var3 = false;

      try {
         int var4 = var1.lastIndexOf(47);
         if (!var1.substring(var4 + 1).equals("text()") && var1.charAt(var4 + 1) != '@') {
            throw new ParseException("Last step of Xpath expression \"" + var1 + "\" is not \"text()\" and does not start with a '@'. It starts with a '" + var1.charAt(var4 + 1) + "'");
         } else {
            String var5 = var1.substring(0, var4);
            if (var1.charAt(var4 + 1) == '@') {
               String var6 = var1.substring(var4 + 2);
               if (var6.length() == 0) {
                  throw new ParseException("Xpath expression \"" + var1 + "\" specifies zero-length attribute name\"");
               } else {
                  Enumeration var7 = this.xpathSelectElements(var5);

                  while(var7.hasMoreElements()) {
                     Element var8 = (Element)var7.nextElement();
                     String var9 = var8.getAttribute(var6);
                     if (!var2.equals(var9)) {
                        var8.setAttribute(var6, var2);
                        var3 = true;
                     }
                  }

                  return var3;
               }
            } else {
               Enumeration var15 = this.xpathSelectElements(var5);
               var3 = var15.hasMoreElements();

               while(true) {
                  while(var15.hasMoreElements()) {
                     Element var16 = (Element)var15.nextElement();
                     Vector var17 = new Vector();

                     for(Node var18 = var16.getFirstChild(); var18 != null; var18 = var18.getNextSibling()) {
                        if (var18 instanceof Text) {
                           var17.addElement((Text)var18);
                        }
                     }

                     Text var10;
                     if (var17.size() == 0) {
                        var10 = new Text(var2);
                        if (var10.getData().length() > 0) {
                           var16.appendChild(var10);
                           var3 = true;
                        }
                     } else {
                        var10 = (Text)var17.elementAt(0);
                        if (!var10.getData().equals(var2)) {
                           var17.removeElementAt(0);
                           var10.setData(var2);
                           var3 = true;
                        }

                        for(int var11 = 0; var11 < var17.size(); ++var11) {
                           Text var12 = (Text)var17.elementAt(var11);
                           var16.removeChild(var12);
                           var3 = true;
                        }
                     }
                  }

                  return var3;
               }
            }
         }
      } catch (DOMException var13) {
         throw new Error("Assertion failed " + var13);
      } catch (IndexOutOfBoundsException var14) {
         throw new ParseException("Xpath expression \"" + var1 + "\" is not in the form \"xpathExpression/@attributeName\"");
      }
   }

   Element makeMatching(final Element var1, Step var2, final String var3) throws ParseException, XPathException {
      NodeTest var4 = var2.getNodeTest();
      if (!(var4 instanceof ElementTest)) {
         throw new ParseException("\"" + var4 + "\" in \"" + var3 + "\" is not an element test");
      } else {
         ElementTest var5 = (ElementTest)var4;
         final String var6 = var5.getTagName();
         final Element var7 = new Element(var6);
         BooleanExpr var8 = var2.getPredicate();
         var8.accept(new BooleanExprVisitor() {
            public void visit(TrueExpr var1x) {
            }

            public void visit(AttrExistsExpr var1x) throws XPathException {
               var7.setAttribute(var1x.getAttrName(), "something");
            }

            public void visit(AttrEqualsExpr var1x) throws XPathException {
               var7.setAttribute(var1x.getAttrName(), var1x.getAttrValue());
            }

            public void visit(AttrNotEqualsExpr var1x) throws XPathException {
               var7.setAttribute(var1x.getAttrName(), "not " + var1x.getAttrValue());
            }

            public void visit(AttrLessExpr var1x) throws XPathException {
               var7.setAttribute(var1x.getAttrName(), Long.toString(Long.MIN_VALUE));
            }

            public void visit(AttrGreaterExpr var1x) throws XPathException {
               var7.setAttribute(var1x.getAttrName(), Long.toString(Long.MAX_VALUE));
            }

            public void visit(TextExistsExpr var1x) throws XPathException {
               var7.appendChild(new Text("something"));
            }

            public void visit(TextEqualsExpr var1x) throws XPathException {
               var7.appendChild(new Text(var1x.getValue()));
            }

            public void visit(TextNotEqualsExpr var1x) throws XPathException {
               var7.appendChild(new Text("not " + var1x.getValue()));
            }

            public void visit(PositionEqualsExpr var1x) throws XPathException {
               int var2 = var1x.getPosition();
               if (var1 == null && var2 != 1) {
                  throw new XPathException(XPath.get(var3), "Position of root node must be 1");
               } else {
                  for(int var3x = 1; var3x < var2; ++var3x) {
                     var1.appendChild(new Element(var6));
                  }

               }
            }
         });
         return var7;
      }
   }

   public abstract Enumeration xpathSelectElements(String var1) throws ParseException;

   public abstract Enumeration xpathSelectStrings(String var1) throws ParseException;

   public abstract Element xpathSelectElement(String var1) throws ParseException;

   public abstract String xpathSelectString(String var1) throws ParseException;

   public abstract Object clone();

   public String toString() {
      try {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         OutputStreamWriter var2 = new OutputStreamWriter(var1);
         this.toString(var2);
         var2.flush();
         return new String(var1.toByteArray());
      } catch (IOException var3) {
         return super.toString();
      }
   }

   abstract void toString(Writer var1) throws IOException;

   abstract void toXml(Writer var1) throws IOException;

   protected static void htmlEncode(Writer var0, String var1) throws IOException {
      int var2 = var1.length();
      int var3 = 0;

      for(int var4 = 0; var4 < var2; ++var4) {
         char var5 = var1.charAt(var4);
         String var6;
         if (var5 >= 128) {
            var6 = "&#" + var5 + ";";
         } else {
            switch(var5) {
            case '"':
               var6 = "&quot;";
               break;
            case '&':
               var6 = "&amp;";
               break;
            case '\'':
               var6 = "&#39;";
               break;
            case '<':
               var6 = "&lt;";
               break;
            case '>':
               var6 = "&gt;";
               break;
            default:
               var6 = null;
            }
         }

         if (var6 != null) {
            var0.write(var1, var3, var4 - var3);
            var0.write(var6);
            var3 = var4 + 1;
         }
      }

      if (var3 < var2) {
         var0.write(var1, var3, var2 - var3);
      }

   }

   protected abstract int computeHashCode();

   public int hashCode() {
      if (this.hash_ == 0) {
         this.hash_ = this.computeHashCode();
      }

      return this.hash_;
   }
}
