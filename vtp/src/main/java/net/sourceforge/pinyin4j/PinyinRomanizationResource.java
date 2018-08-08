package net.sourceforge.pinyin4j;

import net.sourceforge.pinyin4j.sparta.Document;
import net.sourceforge.pinyin4j.sparta.ParseException;
import net.sourceforge.pinyin4j.sparta.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;

class PinyinRomanizationResource {
    private Document pinyinMappingDoc;

    private void setPinyinMappingDoc(Document var1) {
        this.pinyinMappingDoc = var1;
    }

    Document getPinyinMappingDoc() {
        return this.pinyinMappingDoc;
    }

    private PinyinRomanizationResource() {
        this.initializeResource();
    }

    private void initializeResource() {
        try {
            this.setPinyinMappingDoc(Parser.parse("",
                    ResourceHelper.getResourceInputStream(
                            "pinyindb/pinyin_mapping.xml")));
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        } catch (IOException var4) {
            var4.printStackTrace();
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

    }

    static PinyinRomanizationResource getInstance() {
        return PinyinRomanizationSystemResourceHolder.theInstance;
    }

    // $FF: synthetic method
    PinyinRomanizationResource(Object var1) {
        this();
    }

    private static class PinyinRomanizationSystemResourceHolder {
        static final PinyinRomanizationResource theInstance = new PinyinRomanizationResource();
    }
}
