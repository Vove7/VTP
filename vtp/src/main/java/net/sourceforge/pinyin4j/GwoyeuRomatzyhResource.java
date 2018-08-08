package net.sourceforge.pinyin4j;

import net.sourceforge.pinyin4j.sparta.Document;
import net.sourceforge.pinyin4j.sparta.ParseException;
import net.sourceforge.pinyin4j.sparta.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;

class GwoyeuRomatzyhResource {
    private Document pinyinToGwoyeuMappingDoc;

    private void setPinyinToGwoyeuMappingDoc(Document var1) {
        this.pinyinToGwoyeuMappingDoc = var1;
    }

    Document getPinyinToGwoyeuMappingDoc() {
        return this.pinyinToGwoyeuMappingDoc;
    }

    private GwoyeuRomatzyhResource() {
        this.initializeResource();
    }

    private void initializeResource() {
        try {
            this.setPinyinToGwoyeuMappingDoc(Parser.parse("",
                    ResourceHelper.getResourceInputStream(
                            "pinyindb/pinyin_gwoyeu_mapping.xml")));
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        } catch (IOException var4) {
            var4.printStackTrace();
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

    }

    static GwoyeuRomatzyhResource getInstance() {
        return GwoyeuRomatzyhSystemResourceHolder.theInstance;
    }

    // $FF: synthetic method
    GwoyeuRomatzyhResource(Object var1) {
        this();
    }

    private static class GwoyeuRomatzyhSystemResourceHolder {
        static final GwoyeuRomatzyhResource theInstance = new GwoyeuRomatzyhResource();
    }
}
