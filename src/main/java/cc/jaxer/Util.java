package cc.jaxer;

import com.sun.jna.Native;
import com.sun.jna.WString;

import java.io.File;
import java.io.IOException;

public class Util
{
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
    private static String[] unit = new String[]{" B"," K"," M"," G"," T"," P"};

    public static String helpSize(long a){
        double t = a;
        int idx = 0;
        while((t / 1024) > 10){
            t = t/ 1024;
            idx++;
        }
        return t + unit[idx];
    }
}
