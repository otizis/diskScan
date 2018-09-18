package cc.jaxer;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Node
{
    @JSONField(serialize = false)
    private Long id;

    @JSONField(name="value")
    private long size = 0;

    @JSONField(serialize = false)
    private boolean isDir;

    @JSONField(serialize = false)
    private Node parentNode;

    private String name;

    private List<Node> children = null;

    public Node()
    {
        this.id = Util.nodeId.incrementAndGet();
        Util.map.put(this.id, this);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Node{");
        sb.append("id=").append(id);
        sb.append(", size=").append(Util.helpSize(size));
        sb.append(", isDir=").append(isDir);
        sb.append(", name='").append(name).append('\'');
        sb.append(", subNodeSize=").append(children ==null?0: children.size());
        sb.append('}');
        return sb.toString();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public boolean isDir()
    {
        return isDir;
    }

    public void setDir(boolean dir)
    {
        isDir = dir;
    }

    public Node getParentNode()
    {
        return parentNode;
    }

    public void setParentNode(Node parentNode)
    {
        this.parentNode = parentNode;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Node> getChildren()
    {
        return children;
    }

    public void setChildren(List<Node> children)
    {
        this.children = children;
    }
}
