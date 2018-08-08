package net.sourceforge.pinyin4j.sparta.xpath;

import java.io.IOException;
import java.io.Reader;

public class SimpleStreamTokenizer {
   public static final int TT_EOF = -1;
   public static final int TT_NUMBER = -2;
   public static final int TT_WORD = -3;
   public int ttype = Integer.MIN_VALUE;
   public int nval = Integer.MIN_VALUE;
   public String sval = "";
   private static final int WHITESPACE = -5;
   private static final int QUOTE = -6;
   private final StringBuffer buf_ = new StringBuffer();
   private int nextType_;
   private final Reader reader_;
   private final int[] charType_ = new int[256];
   private boolean pushedBack_ = false;
   private char inQuote_ = 0;

   public String toString() {
      switch(this.ttype) {
      case -3:
      case 34:
         return "\"" + this.sval + "\"";
      case -2:
         return Integer.toString(this.nval);
      case -1:
         return "(EOF)";
      case 39:
         return "'" + this.sval + "'";
      default:
         return "'" + (char)this.ttype + "'";
      }
   }

   public SimpleStreamTokenizer(Reader var1) throws IOException {
      this.reader_ = var1;

      for(char var2 = 0; var2 < this.charType_.length; ++var2) {
         if (('A' > var2 || var2 > 'Z') && ('a' > var2 || var2 > 'z') && var2 != '-') {
            if ('0' <= var2 && var2 <= '9') {
               this.charType_[var2] = -2;
            } else if (0 <= var2 && var2 <= ' ') {
               this.charType_[var2] = -5;
            } else {
               this.charType_[var2] = var2;
            }
         } else {
            this.charType_[var2] = -3;
         }
      }

      this.nextToken();
   }

   public void ordinaryChar(char var1) {
      this.charType_[var1] = var1;
   }

   public void wordChars(char var1, char var2) {
      for(char var3 = var1; var3 <= var2; ++var3) {
         this.charType_[var3] = -3;
      }

   }

   public int nextToken() throws IOException {
      if (this.pushedBack_) {
         this.pushedBack_ = false;
         return this.ttype;
      } else {
         this.ttype = this.nextType_;

         while(true) {
            boolean var3 = false;

            int var1;
            int var2;
            boolean var4;
            do {
               var1 = this.reader_.read();
               if (var1 == -1) {
                  if (this.inQuote_ != 0) {
                     throw new IOException("Unterminated quote");
                  }

                  var2 = -1;
               } else {
                  var2 = this.charType_[var1];
               }

               var4 = this.inQuote_ == 0 && var2 == -5;
               var3 = var3 || var4;
            } while(var4);

            if (var2 == 39 || var2 == 34) {
               if (this.inQuote_ == 0) {
                  this.inQuote_ = (char)var2;
               } else if (this.inQuote_ == var2) {
                  this.inQuote_ = 0;
               }
            }

            if (this.inQuote_ != 0) {
               var2 = this.inQuote_;
            }

            var3 = var3 || this.ttype >= -1 && this.ttype != 39 && this.ttype != 34 || this.ttype != var2;
            if (var3) {
               switch(this.ttype) {
               case -3:
                  this.sval = this.buf_.toString();
                  this.buf_.setLength(0);
                  break;
               case -2:
                  this.nval = Integer.parseInt(this.buf_.toString());
                  this.buf_.setLength(0);
                  break;
               case 34:
               case 39:
                  this.sval = this.buf_.toString().substring(1, this.buf_.length() - 1);
                  this.buf_.setLength(0);
               }

               if (var2 != -5) {
                  this.nextType_ = var2 == -6 ? var1 : var2;
               }
            }

            switch(var2) {
            case -3:
            case -2:
            case 34:
            case 39:
               this.buf_.append((char)var1);
            default:
               if (var3) {
                  return this.ttype;
               }
            }
         }
      }
   }

   public void pushBack() {
      this.pushedBack_ = true;
   }
}
