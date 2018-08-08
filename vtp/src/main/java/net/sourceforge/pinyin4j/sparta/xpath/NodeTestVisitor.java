package net.sourceforge.pinyin4j.sparta.xpath;

public interface NodeTestVisitor {
   void visit(AllElementTest var1);

   void visit(ThisNodeTest var1);

   void visit(ParentNodeTest var1) throws XPathException;

   void visit(ElementTest var1);

   void visit(AttrTest var1);

   void visit(TextTest var1);
}
