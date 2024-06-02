package urinov.shz.kunuz.auth.email;

import org.springframework.data.repository.CrudRepository;


import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailHistoryRepository  extends CrudRepository<EmailHistoryEntity,Integer> {

    Optional<EmailHistoryEntity> findByMessageAndEmail(String message, String email);

    Long countByEmailAndCreateDateBetween(String email, LocalDateTime from, LocalDateTime to);
}
