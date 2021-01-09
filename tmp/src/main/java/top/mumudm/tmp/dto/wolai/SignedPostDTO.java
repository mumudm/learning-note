package top.mumudm.tmp.dto.wolai;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author mumu
 * @date 2021/1/9 15:40
 */
@NoArgsConstructor
@Data
public class SignedPostDTO {

    private PolicyDataBean policyData;

    private String key;
    private File file;

    @NoArgsConstructor
    @Data
    public static class PolicyDataBean {

        private FormDataBean formData;
        private String url;
        private String bucket;

        @NoArgsConstructor
        @Data
        public static class FormDataBean {
            private String OSSAccessKeyId;
            private String Signature;
            private String policy;
            private String xosssecuritytoken;
        }
    }
}
