package cc.jaxer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * @author jaxer
 */
public class NodeSearchDownTask implements Runnable
{

    private static Logger logger = LogManager.getLogger(App.class);

    private File dirFile;
    private Node parentNode;

    public NodeSearchDownTask(File dirFile, Node parentNode)
    {
        this.parentNode = parentNode;
        this.dirFile = dirFile;
    }

    @Override
    public void run()
    {
        Node me = new Node();
        me.setName(dirFile.getName());
        me.setDir(dirFile.isDirectory());
        me.setSize(dirFile.length());


        synchronized (parentNode){
            if(this.parentNode.getChildren() == null){
                this.parentNode.setChildren(new ArrayList<Node>());
            }
            this.parentNode.getChildren().add(me);
        }

        me.setParentNode(this.parentNode);

        if (me.isDir())
        {
            File[] files = dirFile.listFiles();
            if (files == null)
            {
                return;
            }

            for (File file : files)
            {
                if (!Util.isJunctionOrSymlink(file))
                {
                    NodeSearchDownTask nodeSearchDownTask = new NodeSearchDownTask(file, me);
                    App.pendingFileVisits.incrementAndGet();
                    App.service.execute(nodeSearchDownTask);
                }
            }
        }

        long l = App.pendingFileVisits.decrementAndGet();
        logger.debug(l);
    }
}
