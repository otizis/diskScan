package cc.jaxer;

import com.sun.jna.Library;
import com.sun.jna.WString;

interface Kernel32 extends Library
{
  public int GetFileAttributesW(WString fileName);
}
