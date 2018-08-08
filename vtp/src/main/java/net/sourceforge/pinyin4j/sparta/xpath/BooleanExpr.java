package net.sourceforge.pinyin4j.sparta.xpath;

public abstract class BooleanExpr {
   public abstract void accept(BooleanExprVisitor var1) throws XPathException;
}
