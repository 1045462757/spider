package com.example.spider;

import com.example.component.Visitor;
import com.example.domain.Link;
import com.example.domain.Page;
import com.example.store.LinkStore;
import com.example.store.PageStore;
import com.example.utility.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

/**
 * @author 10454
 */
public class JdlyVisitor extends Visitor implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 停止标识
     */
    public static boolean stop = false;

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName() + "开始工作");

        // 有效数量
        int goodWork = 0;
        // 无效数量
        int badWork = 0;

        // 记录开始时间
        long start = System.currentTimeMillis();

        while (!stop) {

            // 不满足工作要求
            while (LinkStore.getInstance().isUnvisitedEmpty()) {

                // 收到结束通知
                if (stop) {
                    break;
                }

                // 进入等待状态
                Config.lock.lock();
                try {
                    Config.visitor.await();
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
            Link link = LinkStore.getInstance().getFirst();
            if (link == null) {
                continue;
            }
            Page page = visitForGet(link.getUrl());
            if (page == null) {
                badWork++;
                continue;
            }

            // 工作正常
            PageStore.getInstance().addUnVisitedPage(page);

            LinkStore.getInstance().addVisitedLink(link);

            goodWork++;

            // 收到结束通知时，取消结束
            if (Config.END) {
                Config.END = false;
            }

            // 通知一个parser工作
            Config.lock.lock();
            Config.parser.signal();
            Config.lock.unlock();
        }

        close();

        // 记录结束时间
        long end = System.currentTimeMillis();
        long diff = end - start;
        DecimalFormat df = new DecimalFormat("0.00");

        logger.info(Thread.currentThread().getName() + " " + "停止工作,共访问了"
                + (goodWork + badWork) + "条链接,有效数量:" + goodWork + ",无效数量:" + badWork + ",共耗时" + diff / 1000 + "秒,平均时间为:"
                + df.format((float) (diff / 1000) / (goodWork + badWork)) + "秒");
    }
}
