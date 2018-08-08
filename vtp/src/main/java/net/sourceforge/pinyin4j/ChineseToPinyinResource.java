package net.sourceforge.pinyin4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

class ChineseToPinyinResource {
    private Properties unicodeToHanyuPinyinTable;

    private void setUnicodeToHanyuPinyinTable(Properties var1) {
        this.unicodeToHanyuPinyinTable = var1;
    }

    private Properties getUnicodeToHanyuPinyinTable() {
        return this.unicodeToHanyuPinyinTable;
    }

    private ChineseToPinyinResource() {
        this.unicodeToHanyuPinyinTable = null;
        this.initializeResource();
    }

    private void initializeResource() {
        try {
            this.setUnicodeToHanyuPinyinTable(new Properties());
            this.getUnicodeToHanyuPinyinTable().load(ResourceHelper.getResourceInputStream(
                    "pinyindb/unicode_to_hanyu_pinyin.txt"));
        } catch (FileNotFoundException var2) {
            var2.printStackTrace();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    String[] getHanyuPinyinStringArray(char var1) {
        String var2 = this.getHanyuPinyinRecordFromChar(var1);
        if (null != var2) {
            int var3 = var2.indexOf("(");
            int var4 = var2.lastIndexOf(")");
            String var5 = var2.substring(var3 + "(".length(), var4);
            return var5.split(",");
        } else {
            return null;
        }
    }

    private boolean isValidRecord(String var1) {
        return null != var1 && !var1.equals("(none0)") && var1.startsWith("(") && var1.endsWith(")");
    }

    private String getHanyuPinyinRecordFromChar(char var1) {
        String var3 = Integer.toHexString(var1).toUpperCase();
        String var4 = this.getUnicodeToHanyuPinyinTable().getProperty(var3);
        return this.isValidRecord(var4) ? var4 : null;
    }

    static ChineseToPinyinResource getInstance() {
        return ChineseToPinyinResourceHolder.theInstance;
    }

    // $FF: synthetic method
    ChineseToPinyinResource(Object var1) {
        this();
    }

    class Field {
        static final String LEFT_BRACKET = "(";
        static final String RIGHT_BRACKET = ")";
        static final String COMMA = ",";
    }

    private static class ChineseToPinyinResourceHolder {
        static final ChineseToPinyinResource theInstance = new ChineseToPinyinResource();
    }
}
