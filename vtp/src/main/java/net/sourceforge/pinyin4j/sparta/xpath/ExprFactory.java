package net.sourceforge.pinyin4j.sparta.xpath;

import java.io.IOException;

public class ExprFactory {
   static BooleanExpr createExpr(XPath var0, SimpleStreamTokenizer var1) throws XPathException, IOException {
      switch(var1.ttype) {
      case -3:
         if (!var1.sval.equals("text")) {
            throw new XPathException(var0, "at beginning of expression", var1, "text()");
         } else if (var1.nextToken() != 40) {
            throw new XPathException(var0, "after text", var1, "(");
         } else if (var1.nextToken() != 41) {
            throw new XPathException(var0, "after text(", var1, ")");
         } else {
            String var6;
            switch(var1.nextToken()) {
            case 33:
               var1.nextToken();
               if (var1.ttype != 61) {
                  throw new XPathException(var0, "after !", var1, "=");
               } else {
                  var1.nextToken();
                  if (var1.ttype != 34 && var1.ttype != 39) {
                     throw new XPathException(var0, "right hand side of !=", var1, "quoted string");
                  }

                  var6 = var1.sval;
                  var1.nextToken();
                  return new TextNotEqualsExpr(var6);
               }
            case 61:
               var1.nextToken();
               if (var1.ttype != 34 && var1.ttype != 39) {
                  throw new XPathException(var0, "right hand side of equals", var1, "quoted string");
               }

               var6 = var1.sval;
               var1.nextToken();
               return new TextEqualsExpr(var6);
            default:
               return TextExistsExpr.INSTANCE;
            }
         }
      case -2:
         int var2 = var1.nval;
         var1.nextToken();
         return new PositionEqualsExpr(var2);
      case 64:
         if (var1.nextToken() != -3) {
            throw new XPathException(var0, "after @", var1, "name");
         } else {
            String var3 = var1.sval;
            String var4;
            int var5;
            switch(var1.nextToken()) {
            case 33:
               var1.nextToken();
               if (var1.ttype != 61) {
                  throw new XPathException(var0, "after !", var1, "=");
               } else {
                  var1.nextToken();
                  if (var1.ttype != 34 && var1.ttype != 39) {
                     throw new XPathException(var0, "right hand side of !=", var1, "quoted string");
                  }

                  var4 = var1.sval;
                  var1.nextToken();
                  return new AttrNotEqualsExpr(var3, var4);
               }
            case 60:
               var1.nextToken();
               if (var1.ttype != 34 && var1.ttype != 39) {
                  if (var1.ttype != -2) {
                     throw new XPathException(var0, "right hand side of less-than", var1, "quoted string or number");
                  }

                  var5 = var1.nval;
               } else {
                  var5 = Integer.parseInt(var1.sval);
               }

               var1.nextToken();
               return new AttrLessExpr(var3, var5);
            case 61:
               var1.nextToken();
               if (var1.ttype != 34 && var1.ttype != 39) {
                  throw new XPathException(var0, "right hand side of equals", var1, "quoted string");
               }

               var4 = var1.sval;
               var1.nextToken();
               return new AttrEqualsExpr(var3, var4);
            case 62:
               var1.nextToken();
               if (var1.ttype != 34 && var1.ttype != 39) {
                  if (var1.ttype != -2) {
                     throw new XPathException(var0, "right hand side of greater-than", var1, "quoted string or number");
                  }

                  var5 = var1.nval;
               } else {
                  var5 = Integer.parseInt(var1.sval);
               }

               var1.nextToken();
               return new AttrGreaterExpr(var3, var5);
            default:
               return new AttrExistsExpr(var3);
            }
         }
      default:
         throw new XPathException(var0, "at beginning of expression", var1, "@, number, or text()");
      }
   }
}
