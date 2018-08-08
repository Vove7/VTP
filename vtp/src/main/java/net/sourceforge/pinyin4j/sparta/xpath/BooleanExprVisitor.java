package net.sourceforge.pinyin4j.sparta.xpath;

public interface BooleanExprVisitor {
   void visit(TrueExpr var1);

   void visit(AttrExistsExpr var1) throws XPathException;

   void visit(AttrEqualsExpr var1) throws XPathException;

   void visit(AttrNotEqualsExpr var1) throws XPathException;

   void visit(AttrLessExpr var1) throws XPathException;

   void visit(AttrGreaterExpr var1) throws XPathException;

   void visit(TextExistsExpr var1) throws XPathException;

   void visit(TextEqualsExpr var1) throws XPathException;

   void visit(TextNotEqualsExpr var1) throws XPathException;

   void visit(PositionEqualsExpr var1) throws XPathException;
}
