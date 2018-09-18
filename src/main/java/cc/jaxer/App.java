package cc.jaxer;

import java.io.File;

/**
 * 计算某个目录下的文件，统计该文件夹的大小
 */
public class App
{
    public static void main(String[] args)
    {
        Util.scan(new File("C:"));
    }
}
