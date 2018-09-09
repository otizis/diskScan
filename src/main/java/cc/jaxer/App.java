package cc.jaxer;


import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 计算某个目录下的文件，统计该文件夹的大小
 */
public class App
{
    private static Logger logger = LogManager.getLogger(App.class);

    public static ExecutorService service = Executors.newFixedThreadPool(8);

    public final static AtomicLong pendingFileVisits = new AtomicLong();

    public final static AtomicLong nodeId = new AtomicLong(1);

    public final static ConcurrentHashMap<Long, Node> map = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException
    {
        long start = System.currentTimeMillis();
        File dir = new File("D:\\Resource\\MyProject");
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
        System.out.println("find file size:" + map.size());
        System.out.println("space: " + Util.helpSize(rootNode.getSize()));
        System.out.println("spend: " + (System.currentTimeMillis() - start)/1000d);

        List<Node> subNode = rootNode.getChildren().get(0).getChildren();
        for (Node node : subNode)
        {
            System.out.println(node.toString());
        }
        String s = JSON.toJSONString(rootNode);
        FileUtils.writeStringToFile(new File("diskScan.json"),s,"utf8");
    }


    private static void countTotalNode(Node node)
    {
        List<Node> subNodeList = node.getChildren();
        if(subNodeList==null){
            return;
        }
        try
        {
            for (Node subNode : subNodeList)
            {
                if (subNode.isDir())
                {
                    countTotalNode(subNode);
                }
                node.setSize(node.getSize() + subNode.getSize());
            }
        }
        catch (Exception e)
        {
            logger.error("error", e);
        }

    }

}
