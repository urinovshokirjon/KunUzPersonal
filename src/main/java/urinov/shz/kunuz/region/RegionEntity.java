package urinov.shz.kunuz.region;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import urinov.shz.kunuz.abstracts.CommonEntity;

@Setter
@Getter
@Entity
@Table(name = "region")
public class RegionEntity extends CommonEntity {

}
