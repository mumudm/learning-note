package top.mumudm.tmp.wolai;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import top.mumudm.tmp.dto.wolai.GetSignedPostUrlDTO;
import top.mumudm.tmp.dto.wolai.ImportQueueDTO;
import top.mumudm.tmp.dto.wolai.SignedPostDTO;
import top.mumudm.tmp.util.wolai.WoLaiUtil;
import top.mumudm.tmp.util.wolai.file.FileComparator;
import top.mumudm.tmp.util.wolai.file.FileNameComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mumu
 * @date 2021/1/9 13:34
 */
public class WoLai {

    @Test
    public void importHtml(){
        String path = "/Users/mumu/learn/geekdoc/";
        File rootDir = FileUtil.newFile(path);
        String[] dirs = rootDir.list();

        ArrayList<String> dirList = CollUtil.newArrayList(dirs);
        CollUtil.sort(dirList, new FileNameComparator());
        for (String dir : dirList) {
            if (StrUtil.startWith(dir, ".")) {
                continue;
            }
            System.out.println(dir);

            List<ImportQueueDTO> list = new LinkedList<>();
            List<File> files = FileUtil.loopFiles(path + dir + "/" + "tmp");
            CollUtil.sort(files, new FileComparator());
            String parentId = WoLaiUtil.creatEmptyPage(dir);
            for (File file : files) {
                GetSignedPostUrlDTO postUrlDTO = new GetSignedPostUrlDTO();
                postUrlDTO.setFileSize(file.length());

                SignedPostDTO postUrl = WoLaiUtil.getSignedPostUrl(postUrlDTO, file);
                WoLaiUtil.fileUpload(postUrl);

                ImportQueueDTO queueDTO = new ImportQueueDTO();
                queueDTO.setFilename(postUrl.getKey());
                queueDTO.setPageTitle(FileNameUtil.getPrefix(file));
                queueDTO.setBucket(postUrl.getPolicyData().getBucket());
                list.add(queueDTO);

                if(list.size() >= 20){
                    WoLaiUtil.getBatchImportPageData(list, parentId);
                    list.clear();
                }
            }
            if(CollUtil.isNotEmpty(list)){
                WoLaiUtil.getBatchImportPageData(list, parentId);
            }
        }
    }

    @Test
    public void t() {
        List<File> files = FileUtil.loopFiles("/Users/mumu/learn/geekdoc/161-手机摄影/tmp");
        CollUtil.sort(files, new FileComparator());
        List<ImportQueueDTO> list = new LinkedList<>();

        for (File file : files) {
            GetSignedPostUrlDTO postUrlDTO = new GetSignedPostUrlDTO();
            postUrlDTO.setFileSize(file.length());

            SignedPostDTO postUrl = WoLaiUtil.getSignedPostUrl(postUrlDTO, file);
            WoLaiUtil.fileUpload(postUrl);

            ImportQueueDTO queueDTO = new ImportQueueDTO();
            queueDTO.setFilename(postUrl.getKey());
            queueDTO.setPageTitle(file.getName());
            queueDTO.setBucket(postUrl.getPolicyData().getBucket());
            list.add(queueDTO);
        }
        System.out.println(JSON.toJSONString(list));
        WoLaiUtil.getBatchImportPageData(list, WoLaiUtil.creatEmptyPage("161-手机摄影"));
    }

    @Test
    public void a(){
        ArrayList<Integer> list = CollUtil.newArrayList(1, 2, 23, 43, 43, 43, 3, 4, 343, 3);
        System.out.println(list.toString());
        list.clear();
        list.add(3);
        System.out.println(list);
    }


}
