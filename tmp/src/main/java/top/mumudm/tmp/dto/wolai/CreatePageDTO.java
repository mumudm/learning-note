package top.mumudm.tmp.dto.wolai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mumu
 * @date 2021/1/9 15:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePageDTO {

    private String spaceId;
    private String type;
    private String parentPageId;
    private List<ImportQueueDTO> queue;

}
