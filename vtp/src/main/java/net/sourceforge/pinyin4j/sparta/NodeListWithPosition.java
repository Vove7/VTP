package net.sourceforge.pinyin4j.sparta;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

class NodeListWithPosition {
   private static final Integer ONE = new Integer(1);
   private static final Integer TWO = new Integer(2);
   private static final Integer THREE = new Integer(3);
   private static final Integer FOUR = new Integer(4);
   private static final Integer FIVE = new Integer(5);
   private static final Integer SIX = new Integer(6);
   private static final Integer SEVEN = new Integer(7);
   private static final Integer EIGHT = new Integer(8);
   private static final Integer NINE = new Integer(9);
   private static final Integer TEN = new Integer(10);
   private final Vector vector_ = new Vector();
   private Hashtable positions_ = new Hashtable();

   Enumeration iterator() {
      return this.vector_.elements();
   }

   void removeAllElements() {
      this.vector_.removeAllElements();
      this.positions_.clear();
   }

   void add(String var1) {
      this.vector_.addElement(var1);
   }

   private static Integer identity(Node var0) {
      return new Integer(System.identityHashCode(var0));
   }

   void add(Node var1, int var2) {
      this.vector_.addElement(var1);
      Integer var3;
      switch(var2) {
      case 1:
         var3 = ONE;
         break;
      case 2:
         var3 = TWO;
         break;
      case 3:
         var3 = THREE;
         break;
      case 4:
         var3 = FOUR;
         break;
      case 5:
         var3 = FIVE;
         break;
      case 6:
         var3 = SIX;
         break;
      case 7:
         var3 = SEVEN;
         break;
      case 8:
         var3 = EIGHT;
         break;
      case 9:
         var3 = NINE;
         break;
      case 10:
         var3 = TEN;
         break;
      default:
         var3 = new Integer(var2);
      }

      this.positions_.put(identity(var1), var3);
   }

   int position(Node var1) {
      return (Integer)this.positions_.get(identity(var1));
   }

   public String toString() {
      try {
         StringBuffer var1 = new StringBuffer("{ ");
         Enumeration var2 = this.vector_.elements();

         while(var2.hasMoreElements()) {
            Object var3 = var2.nextElement();
            if (var3 instanceof String) {
               var1.append("String(" + var3 + ") ");
            } else {
               Node var4 = (Node)var3;
               var1.append("Node(" + var4.toXml() + ")[" + this.positions_.get(identity(var4)) + "] ");
            }
         }

         var1.append("}");
         return var1.toString();
      } catch (IOException var5) {
         return var5.toString();
      }
   }
}
