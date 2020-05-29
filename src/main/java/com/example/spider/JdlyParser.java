package com.example.spider;

import com.example.component.Parser;
import com.example.domain.Link;
import com.example.domain.Page;
import com.example.store.LinkStore;
import com.example.store.PageStore;
import com.example.utility.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 10454
 */
public class JdlyParser extends Parser implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 停止标识
     */
    private boolean stop = false;

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName() + "开始工作");

        // 有效数量
        int goodWork = 0;
        // 无效数量
        int badWork = 0;

        // 记录开始时间
        long start = System.currentTimeMillis();

        // 等待PageStore注入
        Config.lock.lock();
        try {
            Config.parser.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Config.lock.unlock();

        while (!stop) {

            // 不满足工作要求
            while (PageStore.getInstance().isUnParsedEmpty()) {

                // 工作停止条件判断
                if (LinkStore.getInstance().isUnvisitedEmpty()) {
                    Config.lock.lock();
                    try {
                        Config.END = true;
                        // 防止死锁,5s后自动唤醒
                        Config.end.await(5L, TimeUnit.SECONDS);
                        if (!Config.END) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Config.lock.unlock();
                }

                // 停止通知未被取消则正式停止工作
                if (Config.END) {
                    this.stop = true;
                    JdlyVisitor.stop = true;

                    // 通知所有visitor工作
                    Config.lock.lock();
                    Config.visitor.signalAll();

                    // 防止死锁,2s后自动唤醒
                    try {
                        Config.parser.await(2L, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Config.lock.unlock();

                    break;
                }

                // 进入等待状态
                Config.lock.lock();
                try {
                    Config.parser.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Config.lock.unlock();
            }

            // 开始工作

            // 退出循环
            if (stop) {
                break;
            }

            // 失败直接重来
            Page page = PageStore.getInstance().getFirst();
            if (Objects.isNull(page)) {
                continue;
            }

            Set<Link> allLinks = getLinks(page, "href", "div[id=content]", "a");
            if (Objects.nonNull(allLinks) && allLinks.size() != 0) {
                for (Link link : allLinks) {
                    if (Objects.nonNull(link.getUrl()) && !link.getUrl().contains("user") && !link.getUrl().contains("siscon")) {
                        LinkStore.getInstance().addUnvisitedLink(link);
                    }
                }
            }

            // 图片解析
//            ArrayList<String> arrayList = getImage(page, "src", "div[id=content-innerText]", "img");
//            Map<String, List<String>> map = new HashMap<>();
//            map.put(Utility.removeIllegalCharacter(page.getTitle()), arrayList);
//            ImageStore.getInstance().addImages(map);

            // 工作正常
            PageStore.getInstance().addParsedPages(page);

            goodWork++;

            // 收到结束通知时，取消结束
            if (Config.END) {
                Config.END = false;
            }

            // 通知所有visitor工作
            Config.lock.lock();
            Config.visitor.signalAll();
            Config.lock.unlock();
        }

        // 记录结束时间
        long end = System.currentTimeMillis();
        long diff = end - start;
        DecimalFormat df = new DecimalFormat("0.00");

        logger.info(Thread.currentThread().getName() + " " + "停止工作,共解析了"
                + (goodWork + badWork) + "个页面,有效数量:" + goodWork + ",无效数量:" + badWork + ",共耗时" + diff / 1000 + "秒,平均时间为:"
                + df.format((float) (diff / 1000) / (goodWork + badWork)) + "秒");
    }
}
