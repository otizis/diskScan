package cc.jaxer;


import java.util.ArrayList;
import java.util.List;

public class Node
{
    Long id ;
    long size = 0;
    boolean isDir;
    Node parentNode;
    String name;
    List<Node> subNode = new ArrayList<>();

    public Node(){
        this.id = App.nodeId.incrementAndGet();
        App.map.put(this.id, this);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Node{");
        sb.append("id=").append(id);
        sb.append(", size=").append(Util.helpSize(size));
        sb.append(", isDir=").append(isDir);
        sb.append(", name='").append(name).append('\'');
        sb.append(", subNodeSize=").append(subNode.size());
        sb.append('}');
        return sb.toString();
    }

}
