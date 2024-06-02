package urinov.shz.kunuz.category;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import urinov.shz.kunuz.abstracts.CommonEntity;

@Setter
@Getter
@Entity
@Table(name = "category")
public class CategoryEntity extends CommonEntity {

}
