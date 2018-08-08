package net.sourceforge.pinyin4j.sparta;

import net.sourceforge.pinyin4j.sparta.xpath.AllElementTest;
import net.sourceforge.pinyin4j.sparta.xpath.AttrEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.AttrExistsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.AttrGreaterExpr;
import net.sourceforge.pinyin4j.sparta.xpath.AttrLessExpr;
import net.sourceforge.pinyin4j.sparta.xpath.AttrNotEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.AttrTest;
import net.sourceforge.pinyin4j.sparta.xpath.BooleanExpr;
import net.sourceforge.pinyin4j.sparta.xpath.ElementTest;
import net.sourceforge.pinyin4j.sparta.xpath.ParentNodeTest;
import net.sourceforge.pinyin4j.sparta.xpath.PositionEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.Step;
import net.sourceforge.pinyin4j.sparta.xpath.TextEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.TextExistsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.TextNotEqualsExpr;
import net.sourceforge.pinyin4j.sparta.xpath.TextTest;
import net.sourceforge.pinyin4j.sparta.xpath.ThisNodeTest;
import net.sourceforge.pinyin4j.sparta.xpath.TrueExpr;
import net.sourceforge.pinyin4j.sparta.xpath.Visitor;
import net.sourceforge.pinyin4j.sparta.xpath.XPath;
import net.sourceforge.pinyin4j.sparta.xpath.XPathException;
import java.util.Enumeration;
import java.util.Vector;

class XPathVisitor implements Visitor {
   private static final Boolean TRUE = new Boolean(true);
   private static final Boolean FALSE = new Boolean(false);
   private final NodeListWithPosition nodelistRaw_;
   private Vector nodelistFiltered_;
   private Enumeration nodesetIterator_;
   private Object node_;
   private final BooleanStack exprStack_;
   private Node contextNode_;
   private boolean multiLevel_;
   private XPath xpath_;

   private XPathVisitor(XPath var1, Node var2) throws XPathException {
      this.nodelistRaw_ = new NodeListWithPosition();
      this.nodelistFiltered_ = new Vector();
      this.nodesetIterator_ = null;
      this.node_ = null;
      this.exprStack_ = new BooleanStack();
      this.xpath_ = var1;
      this.contextNode_ = var2;
      this.nodelistFiltered_ = new Vector(1);
      this.nodelistFiltered_.addElement(this.contextNode_);
      Enumeration var3 = var1.getSteps();

      while(var3.hasMoreElements()) {
         Step var4 = (Step)var3.nextElement();
         this.multiLevel_ = var4.isMultiLevel();
         this.nodesetIterator_ = null;
         var4.getNodeTest().accept(this);
         this.nodesetIterator_ = this.nodelistRaw_.iterator();
         this.nodelistFiltered_.removeAllElements();
         BooleanExpr var5 = var4.getPredicate();

         while(this.nodesetIterator_.hasMoreElements()) {
            this.node_ = this.nodesetIterator_.nextElement();
            var5.accept(this);
            Boolean var6 = this.exprStack_.pop();
            if (var6) {
               this.nodelistFiltered_.addElement(this.node_);
            }
         }
      }

   }

   public XPathVisitor(Element var1, XPath var2) throws XPathException {
      this((XPath)var2, (Node)var1);
      if (var2.isAbsolute()) {
         throw new XPathException(var2, "Cannot use element as context node for absolute xpath");
      }
   }

   public XPathVisitor(Document var1, XPath var2) throws XPathException {
      this((XPath)var2, (Node)var1);
   }

   public void visit(ThisNodeTest var1) {
      this.nodelistRaw_.removeAllElements();
      this.nodelistRaw_.add(this.contextNode_, 1);
   }

   public void visit(ParentNodeTest var1) throws XPathException {
      this.nodelistRaw_.removeAllElements();
      Element var2 = this.contextNode_.getParentNode();
      if (var2 == null) {
         throw new XPathException(this.xpath_, "Illegal attempt to apply \"..\" to node with no parent.");
      } else {
         this.nodelistRaw_.add(var2, 1);
      }
   }

   public void visit(AllElementTest var1) {
      Vector var2 = this.nodelistFiltered_;
      this.nodelistRaw_.removeAllElements();
      Enumeration var3 = var2.elements();

      while(var3.hasMoreElements()) {
         Object var4 = var3.nextElement();
         if (var4 instanceof Element) {
            this.accumulateElements((Element)var4);
         } else if (var4 instanceof Document) {
            this.accumulateElements((Document)var4);
         }
      }

   }

   private void accumulateElements(Document var1) {
      Element var2 = var1.getDocumentElement();
      this.nodelistRaw_.add(var2, 1);
      if (this.multiLevel_) {
         this.accumulateElements(var2);
      }

   }

   private void accumulateElements(Element var1) {
      int var2 = 0;

      for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3 instanceof Element) {
            ++var2;
            this.nodelistRaw_.add(var3, var2);
            if (this.multiLevel_) {
               this.accumulateElements((Element)var3);
            }
         }
      }

   }

   public void visit(TextTest var1) {
      Vector var2 = this.nodelistFiltered_;
      this.nodelistRaw_.removeAllElements();
      Enumeration var3 = var2.elements();

      while(true) {
         Object var4;
         do {
            if (!var3.hasMoreElements()) {
               return;
            }

            var4 = var3.nextElement();
         } while(!(var4 instanceof Element));

         Element var5 = (Element)var4;

         for(Node var6 = var5.getFirstChild(); var6 != null; var6 = var6.getNextSibling()) {
            if (var6 instanceof Text) {
               this.nodelistRaw_.add(((Text)var6).getData());
            }
         }
      }
   }

   public void visit(ElementTest var1) {
      String var2 = var1.getTagName();
      Vector var3 = this.nodelistFiltered_;
      int var4 = var3.size();
      this.nodelistRaw_.removeAllElements();

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3.elementAt(var5);
         if (var6 instanceof Element) {
            this.accumulateMatchingElements((Element)var6, var2);
         } else if (var6 instanceof Document) {
            this.accumulateMatchingElements((Document)var6, var2);
         }
      }

   }

   private void accumulateMatchingElements(Document var1, String var2) {
      Element var3 = var1.getDocumentElement();
      if (var3 != null) {
         if (var3.getTagName() == var2) {
            this.nodelistRaw_.add(var3, 1);
         }

         if (this.multiLevel_) {
            this.accumulateMatchingElements(var3, var2);
         }

      }
   }

   private void accumulateMatchingElements(Element var1, String var2) {
      int var3 = 0;

      for(Node var4 = var1.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getTagName() == var2) {
               ++var3;
               this.nodelistRaw_.add(var5, var3);
            }

            if (this.multiLevel_) {
               this.accumulateMatchingElements(var5, var2);
            }
         }
      }

   }

   public void visit(AttrTest var1) {
      Vector var2 = this.nodelistFiltered_;
      this.nodelistRaw_.removeAllElements();
      Enumeration var3 = var2.elements();

      while(var3.hasMoreElements()) {
         Node var4 = (Node)var3.nextElement();
         if (var4 instanceof Element) {
            Element var5 = (Element)var4;
            String var6 = var5.getAttribute(var1.getAttrName());
            if (var6 != null) {
               this.nodelistRaw_.add(var6);
            }
         }
      }

   }

   public void visit(TrueExpr var1) {
      this.exprStack_.push(TRUE);
   }

   public void visit(AttrExistsExpr var1) throws XPathException {
      if (!(this.node_ instanceof Element)) {
         throw new XPathException(this.xpath_, "Cannot test attribute of document");
      } else {
         Element var2 = (Element)this.node_;
         String var3 = var2.getAttribute(var1.getAttrName());
         boolean var4 = var3 != null && var3.length() > 0;
         this.exprStack_.push(var4 ? TRUE : FALSE);
      }
   }

   public void visit(AttrEqualsExpr var1) throws XPathException {
      if (!(this.node_ instanceof Element)) {
         throw new XPathException(this.xpath_, "Cannot test attribute of document");
      } else {
         Element var2 = (Element)this.node_;
         String var3 = var2.getAttribute(var1.getAttrName());
         boolean var4 = var1.getAttrValue().equals(var3);
         this.exprStack_.push(var4 ? TRUE : FALSE);
      }
   }

   public void visit(AttrNotEqualsExpr var1) throws XPathException {
      if (!(this.node_ instanceof Element)) {
         throw new XPathException(this.xpath_, "Cannot test attribute of document");
      } else {
         Element var2 = (Element)this.node_;
         String var3 = var2.getAttribute(var1.getAttrName());
         boolean var4 = !var1.getAttrValue().equals(var3);
         this.exprStack_.push(var4 ? TRUE : FALSE);
      }
   }

   public void visit(AttrLessExpr var1) throws XPathException {
      if (!(this.node_ instanceof Element)) {
         throw new XPathException(this.xpath_, "Cannot test attribute of document");
      } else {
         Element var2 = (Element)this.node_;
         long var3 = Long.parseLong(var2.getAttribute(var1.getAttrName()));
         boolean var5 = (double)var3 < var1.getAttrValue();
         this.exprStack_.push(var5 ? TRUE : FALSE);
      }
   }

   public void visit(AttrGreaterExpr var1) throws XPathException {
      if (!(this.node_ instanceof Element)) {
         throw new XPathException(this.xpath_, "Cannot test attribute of document");
      } else {
         Element var2 = (Element)this.node_;
         long var3 = Long.parseLong(var2.getAttribute(var1.getAttrName()));
         boolean var5 = (double)var3 > var1.getAttrValue();
         this.exprStack_.push(var5 ? TRUE : FALSE);
      }
   }

   public void visit(TextExistsExpr var1) throws XPathException {
      if (!(this.node_ instanceof Element)) {
         throw new XPathException(this.xpath_, "Cannot test attribute of document");
      } else {
         Element var2 = (Element)this.node_;

         for(Node var3 = var2.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3 instanceof Text) {
               this.exprStack_.push(TRUE);
               return;
            }
         }

         this.exprStack_.push(FALSE);
      }
   }

   public void visit(TextEqualsExpr var1) throws XPathException {
      if (!(this.node_ instanceof Element)) {
         throw new XPathException(this.xpath_, "Cannot test attribute of document");
      } else {
         Element var2 = (Element)this.node_;

         for(Node var3 = var2.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3 instanceof Text) {
               Text var4 = (Text)var3;
               if (var4.getData().equals(var1.getValue())) {
                  this.exprStack_.push(TRUE);
                  return;
               }
            }
         }

         this.exprStack_.push(FALSE);
      }
   }

   public void visit(TextNotEqualsExpr var1) throws XPathException {
      if (!(this.node_ instanceof Element)) {
         throw new XPathException(this.xpath_, "Cannot test attribute of document");
      } else {
         Element var2 = (Element)this.node_;

         for(Node var3 = var2.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3 instanceof Text) {
               Text var4 = (Text)var3;
               if (!var4.getData().equals(var1.getValue())) {
                  this.exprStack_.push(TRUE);
                  return;
               }
            }
         }

         this.exprStack_.push(FALSE);
      }
   }

   public void visit(PositionEqualsExpr var1) throws XPathException {
      if (!(this.node_ instanceof Element)) {
         throw new XPathException(this.xpath_, "Cannot test position of document");
      } else {
         Element var2 = (Element)this.node_;
         boolean var3 = this.nodelistRaw_.position(var2) == var1.getPosition();
         this.exprStack_.push(var3 ? TRUE : FALSE);
      }
   }

   public Enumeration getResultEnumeration() {
      return this.nodelistFiltered_.elements();
   }

   public Element getFirstResultElement() {
      return this.nodelistFiltered_.size() == 0 ? null : (Element)this.nodelistFiltered_.elementAt(0);
   }

   public String getFirstResultString() {
      return this.nodelistFiltered_.size() == 0 ? null : this.nodelistFiltered_.elementAt(0).toString();
   }

   private static class BooleanStack {
      private Item top_;

      private BooleanStack() {
         this.top_ = null;
      }

      void push(Boolean var1) {
         this.top_ = new Item(var1, this.top_);
      }

      Boolean pop() {
         Boolean var1 = this.top_.bool;
         this.top_ = this.top_.prev;
         return var1;
      }

      // $FF: synthetic method
      BooleanStack(Object var1) {
         this();
      }

      private static class Item {
         final Boolean bool;
         final Item prev;

         Item(Boolean var1, Item var2) {
            this.bool = var1;
            this.prev = var2;
         }
      }
   }
}
