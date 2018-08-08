package net.sourceforge.pinyin4j.sparta;

import java.util.Enumeration;
import java.util.NoSuchElementException;

class EmptyEnumeration implements Enumeration {
   public boolean hasMoreElements() {
      return false;
   }

   public Object nextElement() {
      throw new NoSuchElementException();
   }
}
