package net.sourceforge.pinyin4j.sparta.xpath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

public class XPath {
   private static final int ASSERTION = 0;
   private Stack steps_;
   private boolean absolute_;
   private String string_;
   private static Hashtable cache_ = new Hashtable();

   private XPath(boolean var1, Step[] var2) {
      this.steps_ = new Stack();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.steps_.addElement(var2[var3]);
      }

      this.absolute_ = var1;
      this.string_ = null;
   }

   private XPath(String var1) throws XPathException {
      this(var1, new InputStreamReader(new ByteArrayInputStream(var1.getBytes())));
   }

   private XPath(String var1, Reader var2) throws XPathException {
      this.steps_ = new Stack();

      try {
         this.string_ = var1;
         SimpleStreamTokenizer var3 = new SimpleStreamTokenizer(var2);
         var3.ordinaryChar('/');
         var3.ordinaryChar('.');
         var3.wordChars(':', ':');
         var3.wordChars('_', '_');
         boolean var4;
         if (var3.nextToken() == 47) {
            this.absolute_ = true;
            if (var3.nextToken() == 47) {
               var4 = true;
               var3.nextToken();
            } else {
               var4 = false;
            }
         } else {
            this.absolute_ = false;
            var4 = false;
         }

         this.steps_.push(new Step(this, var4, var3));

         for(; var3.ttype == 47; this.steps_.push(new Step(this, var4, var3))) {
            if (var3.nextToken() == 47) {
               var4 = true;
               var3.nextToken();
            } else {
               var4 = false;
            }
         }

         if (var3.ttype != -1) {
            throw new XPathException(this, "at end of XPATH expression", var3, "end of expression");
         }
      } catch (IOException var5) {
         throw new XPathException(this, var5);
      }
   }

   public String toString() {
      if (this.string_ == null) {
         this.string_ = this.generateString();
      }

      return this.string_;
   }

   private String generateString() {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = true;

      for(Enumeration var3 = this.steps_.elements(); var3.hasMoreElements(); var2 = false) {
         Step var4 = (Step)var3.nextElement();
         if (!var2 || this.absolute_) {
            var1.append('/');
            if (var4.isMultiLevel()) {
               var1.append('/');
            }
         }

         var1.append(var4.toString());
      }

      return var1.toString();
   }

   public boolean isAbsolute() {
      return this.absolute_;
   }

   public boolean isStringValue() {
      Step var1 = (Step)this.steps_.peek();
      return var1.isStringValue();
   }

   public Enumeration getSteps() {
      return this.steps_.elements();
   }

   public String getIndexingAttrName() throws XPathException {
      Step var1 = (Step)this.steps_.peek();
      BooleanExpr var2 = var1.getPredicate();
      if (!(var2 instanceof AttrExistsExpr)) {
         throw new XPathException(this, "has no indexing attribute name (must end with predicate of the form [@attrName]");
      } else {
         return ((AttrExistsExpr)var2).getAttrName();
      }
   }

   public String getIndexingAttrNameOfEquals() throws XPathException {
      Step var1 = (Step)this.steps_.peek();
      BooleanExpr var2 = var1.getPredicate();
      return var2 instanceof AttrEqualsExpr ? ((AttrEqualsExpr)var2).getAttrName() : null;
   }

   public Object clone() {
      Step[] var1 = new Step[this.steps_.size()];
      Enumeration var2 = this.steps_.elements();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var1[var3] = (Step)var2.nextElement();
      }

      return new XPath(this.absolute_, var1);
   }

   public static XPath get(String var0) throws XPathException {
      Hashtable var1 = cache_;
      synchronized(cache_) {
         XPath var2 = (XPath)cache_.get(var0);
         if (var2 == null) {
            var2 = new XPath(var0);
            cache_.put(var0, var2);
         }

         return var2;
      }
   }

   public static XPath get(boolean var0, Step[] var1) {
      XPath var2 = new XPath(var0, var1);
      String var3 = var2.toString();
      Hashtable var4 = cache_;
      synchronized(cache_) {
         XPath var5 = (XPath)cache_.get(var3);
         if (var5 == null) {
            cache_.put(var3, var2);
            return var2;
         } else {
            return var5;
         }
      }
   }

   public static boolean isStringValue(String var0) throws XPathException, IOException {
      return get(var0).isStringValue();
   }
}
