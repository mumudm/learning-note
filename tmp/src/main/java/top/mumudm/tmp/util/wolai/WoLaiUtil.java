package top.mumudm.tmp.util.wolai;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;
import top.mumudm.tmp.dto.wolai.CreatePageDTO;
import top.mumudm.tmp.dto.wolai.GetSignedPostUrlDTO;
import top.mumudm.tmp.dto.wolai.ImportQueueDTO;
import top.mumudm.tmp.dto.wolai.SignedPostDTO;

import java.io.File;
import java.util.Collections;
import java.util.List;


/**
 * @author mumu
 * @date 2021/1/9 15:38
 */
@Log
public class WoLaiUtil {

    private static final String COOKIE = "gr_user_id=10bb5449-c9ee-4905-b977-3276537fd577; grwng_uid=49ac7ecb-13f0-4511-bed1-ff74e08e03a1; bd9a53b54d5ce1d4_gr_last_sent_cs1=unmjLMcsnj3Pm6UnUGj3vf; bd9a53b54d5ce1d4_gr_cs1=unmjLMcsnj3Pm6UnUGj3vf; token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1bm1qTE1jc25qM1BtNlVuVUdqM3ZmIiwiaWF0IjoxNjA4NzkwMzI4LCJleHAiOjE2MTY1NjYzMjh9.DBZjPThbXglG4XwF2wiMtvduzpracHg6GkAQPY20LZo; Hm_lvt_e8e295cdb34660fdc35eb734b7bd5846=1607305858,1608515541,1609120072; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22unmjLMcsnj3Pm6UnUGj3vf%22%2C%22first_id%22%3A%22176692bc3fb22-00a4ca8c8935c-163a6153-1764000-176692bc3fca67%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%22176692bc3fb22-00a4ca8c8935c-163a6153-1764000-176692bc3fca67%22%7D";

    // dxc  geektime
    private static final String spaceId = "kpoFXdq8pWbZyUMPiAW1cM";
    private static final String type = "html";
    private static final String parentPageId = "t4Z7Fi7qKqhbvqWTAfoiKe";

    // 6297 tmp
    // private static final String spaceId = "rauuAsCnueCGY8NG9HtL9K";
    // private static final String type = "html";
    // private static final String parentPageId = "2tMz2zQiC9xJ6sQ1NsRqSB";

    private static final String bucket = "wolai-secure";

    /**
     * 文件上传
     *
     * @param dto dto
     */
    public static void fileUpload(SignedPostDTO dto) {
        SignedPostDTO.PolicyDataBean.FormDataBean formData = dto.getPolicyData().getFormData();
        HttpRequest.post("https://secure-static.wolai.com/")
                .header("Cookie", COOKIE)
                .contentType("multipart/form-data;")
                .form("OSSAccessKeyId", formData.getOSSAccessKeyId())
                .form("Signature", formData.getSignature())
                .form("policy", formData.getPolicy())
                .form("x-oss-security-token", formData.getXosssecuritytoken())
                .form("key", dto.getKey())
                .form("success_action_status", 200)
                .form("file", dto.getFile())
                .execute();
    }

    /**
     * 批量导入页面数据或创建页面
     *
     * @param queue    队列
     * @param parentId 父id
     * @return {@link String}
     */
    public static String getBatchImportPageData(List<ImportQueueDTO> queue, String parentId) {
        CreatePageDTO dto = CreatePageDTO.builder()
                .parentPageId(StrUtil.isBlank(parentId) ? parentPageId : parentId)
                .type(type)
                .spaceId(spaceId)
                .queue(queue)
                .build();
        HttpResponse execute = HttpRequest.post("https://api.wolai.com/v1/import/getBatchImportPageData")
                .header("Cookie", COOKIE)
                .contentType("application/json")
                .body(JSON.toJSONString(dto))
                .execute();
        String body = execute.body();
        JSONObject jo = JSON.parseObject(body);
        return jo.getJSONObject("data").getJSONArray("blocks").toJavaList(JSONObject.class).get(0).getString("id");
    }

    /**
     * 创造空白页
     *
     * @param name 的名字
     * @return {@link String}
     */
    public static String creatEmptyPage(String name) {
        ImportQueueDTO dto = new ImportQueueDTO();
        dto.setBucket(bucket);
        dto.setPageTitle(name);
        return WoLaiUtil.getBatchImportPageData(Collections.singletonList(dto), "");
    }

    /**
     * 获取上传文件参数
     *
     * @param dto  dto
     * @param file 文件
     * @return {@link SignedPostDTO}
     */
    public static SignedPostDTO getSignedPostUrl(GetSignedPostUrlDTO dto, File file) {
        dto.setSpaceId(spaceId);
        dto.setType("import");
        HttpResponse execute = HttpRequest.post("https://api.wolai.com/v1/file/getSignedPostUrl")
                .header("Cookie", COOKIE)
                .contentType(ContentType.JSON.toString())
                .body(JSON.toJSONString(dto))
                .execute();
        JSONObject jo = JSON.parseObject(execute.body(), JSONObject.class);
        SignedPostDTO result = jo.getObject("data", SignedPostDTO.class);
        result.setFile(file);
        result.setKey("import/" + RandomUtil.randomString(22) + "/" + file.getName());
        return result;
    }

}
