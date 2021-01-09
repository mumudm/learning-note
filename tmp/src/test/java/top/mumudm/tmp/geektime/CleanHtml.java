package top.mumudm.tmp.geektime;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author mumu
 * @date 2021/1/7 10:53
 */
public class CleanHtml {


    @Test
    public void readFile() throws Exception {
        List<File> files = FileUtil.loopFiles("/Users/mumu/learn/geekdoc", pathname -> pathname.getName().endsWith(".html"));
        for (File file : files) {
            String path = file.getPath();
            if(StrUtil.contains(path,"tmp")){
                continue;
            }
            String[] split = path.split("/");
            List<String> strings = CollUtil.sub(CollUtil.newArrayList(split), 0, 6);
            String dir = strings.get(strings.size() - 1);
            String join = CollUtil.join(strings, "/") + "/tmp/";
             if(Integer.parseInt(dir.split("-")[0].trim()) <= 95){
                 save(file, join,"_29HP61GA_0");
             }   else {
                 save(file, join,"_2c4hPkl9");
             }
        }

    }

    private static void save(File file, String path,String cla) throws Exception {
        System.out.println("正在处理：" + path + "--" + file.getName());
        Document doc = Jsoup.parse(file, "UTF-8");
        Elements img = doc.getElementsByTag("img");
        for (Element element : img) {
            String attr = element.attr("data-savepage-src");
            element.attr("src", attr);
            element.removeAttr("data-savepage-src");
            element.removeClass("_2273kGdT_0");
            element.removeClass("iconfont _2-nZIZjB_0");
        }
        doc.dataNodes();

        String fileName = file.getName();
        // 正文
        Elements hPkl9 =  doc.getElementsByClass(cla);
        String html = hPkl9.html();
        // 留言
        Elements comment = doc.getElementsByClass("_1qhD3bdE_0 _2QmGFWqF_0");
        String clean = Jsoup.clean(comment.html(), Whitelist.basic());
        String span = HtmlUtil.removeHtmlTag(clean, "span", "i");

        String subSuf = StrUtil.subSuf(span, clean.indexOf('<') + 1);
        String commentHtml = "<h2>留言</h2>\n" + subSuf;

        File newFile = new File(path + fileName);
        FileUtil.touch(newFile);
        FileUtil.writeString(html, newFile, StandardCharsets.UTF_8);
        FileUtil.appendString(commentHtml, newFile, StandardCharsets.UTF_8);
    }
}
