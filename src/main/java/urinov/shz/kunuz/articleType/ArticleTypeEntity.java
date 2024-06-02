package urinov.shz.kunuz.articleType;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import urinov.shz.kunuz.abstracts.CommonEntity;

@Setter
@Getter
@Entity
@Table(name = "article_type")
public class ArticleTypeEntity extends CommonEntity {

}
