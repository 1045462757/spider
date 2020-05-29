package com.example.spider;

import com.example.store.ImageStore;
import com.example.component.ImageDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 10454
 */
public class JdlyImageDownload extends ImageDownload implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        while (!ImageStore.getInstance().isImageMapEmpty()) {
            String name = null;
            List<String> list = null;

            for (Map.Entry<String, List<String>> map : ImageStore.getInstance().getImages().entrySet()) {
                name = map.getKey();
                list = map.getValue();
            }

            ArrayList<String> arrayList = new ArrayList<>();

            for (String s : list) {
                arrayList.add(s);
            }

            download(name, arrayList);
        }
    }
}
