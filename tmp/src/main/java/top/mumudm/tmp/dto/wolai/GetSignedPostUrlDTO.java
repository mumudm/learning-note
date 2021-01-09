package top.mumudm.tmp.dto.wolai;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mumu
 * @date 2021/1/9 16:39
 */
@NoArgsConstructor
@Data
public class GetSignedPostUrlDTO {

    private String spaceId;
    private Long fileSize;
    private String type;
}
