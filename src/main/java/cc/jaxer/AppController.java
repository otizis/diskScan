package cc.jaxer;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AppController
{

    @RequestMapping("/dirList")
    public ArrayList<String> dirList()
    {
        File file = new File("/");
        File[] files = file.listFiles();

        ArrayList<String> strings = new ArrayList<>();

        if(files == null){
            return strings;
        }
        for (File file1 : files)
        {
            String absolutePath = file1.getAbsolutePath();
            strings.add(absolutePath);
        }
        return strings;
    }

    @RequestMapping("/scan")
    public String index(String path)
    {
        if (StringUtils.isEmpty(path))
        {
            path = "D:\\Resource\\YZ_Com";
        }
        Util.scan(new File(path));
        return "ok";
    }

    @RequestMapping("/querySub")
    public List<Node> querySub(Long id)
    {
        Node node = Util.map.get(id);
        return node.getChildren();
    }

}
