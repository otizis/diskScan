package cc.jaxer;


import java.util.ArrayList;
import java.util.List;

public class Node
{
    long size = 0;
    boolean isDir;
    String path;
    Node parentNode;
    String name;
    List<Node> subNode = new ArrayList<>();

    public void print()
    {
        System.out.println(name+"["+size+"]");
        for (Node node : subNode)
        {
            node.print();
        }
    }
}
