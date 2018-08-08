package net.sourceforge.pinyin4j.sparta.xpath;

import java.io.IOException;

public class Step {
   public static Step DOT;
   private final NodeTest nodeTest_;
   private final BooleanExpr predicate_;
   private final boolean multiLevel_;

   Step(NodeTest var1, BooleanExpr var2) {
      this.nodeTest_ = var1;
      this.predicate_ = var2;
      this.multiLevel_ = false;
   }

   Step(XPath var1, boolean var2, SimpleStreamTokenizer var3) throws XPathException, IOException {
      this.multiLevel_ = var2;
      switch(var3.ttype) {
      case -3:
         if (var3.sval.equals("text")) {
            if (var3.nextToken() != 40 || var3.nextToken() != 41) {
               throw new XPathException(var1, "after text", var3, "()");
            }

            this.nodeTest_ = TextTest.INSTANCE;
         } else {
            this.nodeTest_ = new ElementTest(var3.sval);
         }
         break;
      case 42:
         this.nodeTest_ = AllElementTest.INSTANCE;
         break;
      case 46:
         if (var3.nextToken() == 46) {
            this.nodeTest_ = ParentNodeTest.INSTANCE;
         } else {
            var3.pushBack();
            this.nodeTest_ = ThisNodeTest.INSTANCE;
         }
         break;
      case 64:
         if (var3.nextToken() != -3) {
            throw new XPathException(var1, "after @ in node test", var3, "name");
         }

         this.nodeTest_ = new AttrTest(var3.sval);
         break;
      default:
         throw new XPathException(var1, "at begininning of step", var3, "'.' or '*' or name");
      }

      if (var3.nextToken() == 91) {
         var3.nextToken();
         this.predicate_ = ExprFactory.createExpr(var1, var3);
         if (var3.ttype != 93) {
            throw new XPathException(var1, "after predicate expression", var3, "]");
         }

         var3.nextToken();
      } else {
         this.predicate_ = TrueExpr.INSTANCE;
      }

   }

   public String toString() {
      return this.nodeTest_.toString() + this.predicate_.toString();
   }

   public boolean isMultiLevel() {
      return this.multiLevel_;
   }

   public boolean isStringValue() {
      return this.nodeTest_.isStringValue();
   }

   public NodeTest getNodeTest() {
      return this.nodeTest_;
   }

   public BooleanExpr getPredicate() {
      return this.predicate_;
   }

   static {
      DOT = new Step(ThisNodeTest.INSTANCE, TrueExpr.INSTANCE);
   }
}
