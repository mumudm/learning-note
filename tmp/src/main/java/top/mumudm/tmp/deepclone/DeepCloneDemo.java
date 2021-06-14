package top.mumudm.tmp.deepclone;

import com.rits.cloning.Cloner;
import top.mumudm.tmp.dto.wolai.CreatePageDTO;

/**
 * @author mumu
 * @date 2021-6-14 22:47
 */
public class DeepCloneDemo {


    public static void main(String[] args) {
        Cloner cloner = new Cloner();

        CreatePageDTO dto = new CreatePageDTO();
        dto.setType("1");
        dto.setSpaceId("dsf");

        CreatePageDTO clone = cloner.deepClone(dto);
        System.out.println(clone);
        System.out.println(dto);
        System.out.println(clone == dto);
    }
}
