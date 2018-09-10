package cc.jaxer;

import com.sun.jna.Native;
import com.sun.jna.WString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Util
{
    private static Logger logger = LogManager.getLogger(Util.class);

    public static ExecutorService service = Executors.newFixedThreadPool(8);

    public final static AtomicLong pendingFileVisits = new AtomicLong();

    public final static AtomicLong nodeId = new AtomicLong(1);

    public final static ConcurrentHashMap<Long, Node> map = new ConcurrentHashMap<>();

    public static boolean loaded = false;

    static Kernel32 lib = null;

    public static int getWin32FileAttributes(File f) throws IOException
    {
        if (lib == null)
        {
            synchronized (Kernel32.class)
            {
                lib = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
            }
        }
        return lib.GetFileAttributesW(new WString(f.getCanonicalPath()));
    }

    /**
     * 判断文件夹是否是链接
     *
     * @param f
     * @return
     */
    public static boolean isJunctionOrSymlink(File f)
    {
        if (!f.exists())
        {
            return false;
        }
        int attributes = 0;
        try
        {
            attributes = getWin32FileAttributes(f);
        }
        catch (IOException e)
        {
            return true;
        }
        if (-1 == attributes)
        {
            return false;
        }
        return ((0x400 & attributes) != 0);
    }

    private static String[] unit = new String[]{" B", " K", " M", " G", " T", " P"};

    public static String helpSize(long a)
    {
        double t = a;
        int idx = 0;
        while ((t / 1024) > 10)
        {
            t = t / 1024;
            idx++;
        }
        return t + unit[idx];
    }

    public static void scan(File dir)
    {
        if(loaded){
            return;
        }
        loaded = true;

        long start = System.currentTimeMillis();

        logger.info(dir.getAbsolutePath());

        Node rootNode = new Node();
        pendingFileVisits.incrementAndGet();
        service.execute(new NodeSearchDownTask(dir, rootNode));

        try
        {
            while (pendingFileVisits.get() != 0)
            {
                Thread.sleep(1000);
                System.out.print(pendingFileVisits.get() + "-->");
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
        System.out.println("spend: " + (System.currentTimeMillis() - start) / 1000d);

        List<Node> subNode = rootNode.getChildren().get(0).getChildren();
        for (Node node : subNode)
        {
            System.out.println(node.toString());
        }
        //        String s = JSON.toJSONString(subNode);
        //        FileUtils.writeStringToFile(new File("diskScan.json"),s,"utf8");
    }

    private static void countTotalNode(Node node)
    {
        List<Node> subNodeList = node.getChildren();
        if (subNodeList == null)
        {
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
