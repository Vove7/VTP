package net.sourceforge.pinyin4j.sparta;

class CharCircBuffer {
   private final int[] buf_;
   private int next_ = 0;
   private int total_ = 0;
   private boolean enabled_ = true;

   CharCircBuffer(int var1) {
      this.buf_ = new int[var1];
   }

   void enable() {
      this.enabled_ = true;
   }

   void disable() {
      this.enabled_ = false;
   }

   void addInt(int var1) {
      this.addRaw(var1 + 65536);
   }

   void addChar(char var1) {
      this.addRaw(var1);
   }

   private void addRaw(int var1) {
      if (this.enabled_) {
         this.buf_[this.next_] = var1;
         this.next_ = (this.next_ + 1) % this.buf_.length;
         ++this.total_;
      }

   }

   void addString(String var1) {
      char[] var2 = var1.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         this.addChar(var2[var4]);
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(11 * this.buf_.length / 10);
      int var2 = this.total_ < this.buf_.length ? this.buf_.length - this.total_ : 0;

      for(int var3 = var2; var3 < this.buf_.length; ++var3) {
         int var4 = (var3 + this.next_) % this.buf_.length;
         int var5 = this.buf_[var4];
         if (var5 < 65536) {
            var1.append((char)var5);
         } else {
            var1.append(Integer.toString(var5 - 65536));
         }
      }

      return var1.toString();
   }
}
