package top.mumudm.tmp.dto.wolai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author mumu
 * @date 2021/1/9 16:21
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ImportQueueDTO {

    private String bucket;
    private String filename;
    private String pageTitle;
}
