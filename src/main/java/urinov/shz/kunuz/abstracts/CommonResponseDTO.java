package urinov.shz.kunuz.abstracts;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CommonResponseDTO {

    private Integer id;

    private Integer orderNumber;

    private String nameUz;

    private String nameRu;

    private String nameEn;

    private String name;

    private Boolean visible;

    private LocalDateTime createDate;
}
