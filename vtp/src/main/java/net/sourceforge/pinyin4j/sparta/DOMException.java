package net.sourceforge.pinyin4j.sparta;

public class DOMException extends Exception {
   public short code;
   public static final short DOMSTRING_SIZE_ERR = 2;
   public static final short HIERARCHY_REQUEST_ERR = 3;
   public static final short NOT_FOUND_ERR = 8;

   public DOMException(short var1, String var2) {
      super(var2);
      this.code = var1;
   }
}
