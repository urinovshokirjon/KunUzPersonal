package urinov.shz.kunuz.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import urinov.shz.kunuz.abstracts.CommonResponseDTO;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto extends CommonResponseDTO {
}
