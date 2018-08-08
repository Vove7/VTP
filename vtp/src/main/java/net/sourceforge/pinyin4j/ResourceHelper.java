package net.sourceforge.pinyin4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

class ResourceHelper {

    static BufferedInputStream getResourceInputStream(String var0) {
        try {
            InputStream is = PinyinHelper.context.getAssets().open(var0);
            return new BufferedInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
