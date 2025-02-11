package urinov.shz.kunuz.articleType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity, Integer> {

    // 3. List ArticleType
    Page<ArticleTypeEntity> findAllByVisibleTrueOrderByOrderNumber(Pageable pageable);

    //
    @Query(value = "SELECT id," +
            " CASE :lang " +
            " WHEN 'UZ' THEN name_uz " +
            " WHEN 'RU' THEN name_ru " +
            " WHEN 'EN' THEN name_en " +
            " END AS name " +
            " FROM article_type ORDER BY order_number DESC ", nativeQuery = true)
    Page<ArticleTypeMapper> getArticleTypePage(@Param("lang") String lang, Pageable pageable);


    List<ArticleTypeEntity> findAllByIdIn(List<Integer> ids);

}

