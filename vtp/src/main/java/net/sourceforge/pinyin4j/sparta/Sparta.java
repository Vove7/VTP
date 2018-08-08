package net.sourceforge.pinyin4j.sparta;

import java.util.Hashtable;

public class Sparta {
   private static Internment internment_ = new Internment() {
      private final Hashtable strings_ = new Hashtable();

      public String intern(String var1) {
         String var2 = (String)this.strings_.get(var1);
         if (var2 == null) {
            this.strings_.put(var1, var1);
            return var1;
         } else {
            return var2;
         }
      }
   };
   private static CacheFactory cacheFactory_ = new CacheFactory() {
      public Cache create() {
         return new HashtableCache();
      }
   };

   public static String intern(String var0) {
      return internment_.intern(var0);
   }

   public static void setInternment(Internment var0) {
      internment_ = var0;
   }

   static Cache newCache() {
      return cacheFactory_.create();
   }

   public static void setCacheFactory(CacheFactory var0) {
      cacheFactory_ = var0;
   }

   private static class HashtableCache extends Hashtable implements Cache {
      private HashtableCache() {
      }

      // $FF: synthetic method
      HashtableCache(Object var1) {
         this();
      }
   }

   public interface CacheFactory {
      Cache create();
   }

   public interface Cache {
      Object get(Object var1);

      Object put(Object var1, Object var2);

      int size();
   }

   public interface Internment {
      String intern(String var1);
   }
}
