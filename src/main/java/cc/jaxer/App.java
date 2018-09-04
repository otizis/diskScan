package cc.jaxer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 计算某个目录下的文件，统计该文件夹的大小
 */
public class App
{
    private static Logger logger = LogManager.getLogger(App.class);

    public static ExecutorService service = Executors.newFixedThreadPool(4);

    public final static AtomicLong pendingFileVisits = new AtomicLong();

    public static void main(String[] args)
    {
        long start = System.currentTimeMillis();
        File dir = new File("D:\\Resource\\YZ_Com");
        logger.info(dir.getAbsolutePath());
        Node rootNode = new Node();
        pendingFileVisits.incrementAndGet();
        service.execute(new NodeSearchDownTask(dir, rootNode));
        try
        {
            while (pendingFileVisits.get() != 0)
            {
                Thread.sleep(1000);
                System.out.print(pendingFileVisits.get()+"-->");
            }
        }
        catch (Exception e)
        {
            logger.error("error ", e);
        }
        service.shutdown();
        countTotalNode(rootNode);
        System.out.println("end");
        System.out.println("size: " + Util.helpSize(rootNode.size));
        System.out.println("spend: " + (System.currentTimeMillis() - start)/1000d);

    }


    private static void countTotalNode(Node node)
    {
        List<Node> subNodeList = node.subNode;
        try
        {
            for (Node subNode : subNodeList)
            {
                if (subNode.isDir)
                {
                    countTotalNode(subNode);
                }
                node.size += subNode.size;
            }
        }
        catch (Exception e)
        {
            logger.error("error", e);
        }

    }

}
