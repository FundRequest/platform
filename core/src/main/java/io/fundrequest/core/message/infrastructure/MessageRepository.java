package io.fundrequest.core.message.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.message.domain.Message;
import io.fundrequest.core.message.domain.MessageType;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByType(MessageType type);

    List<Message> findByType(MessageType type, Sort sort);

    Optional<Message> findByTypeAndName(MessageType type, String name);

    void deleteByTypeAndName(MessageType type, String name);

}
