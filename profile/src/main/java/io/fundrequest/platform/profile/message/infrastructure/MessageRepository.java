package io.fundrequest.platform.profile.message.infrastructure;

import io.fundrequest.platform.profile.message.domain.Message;
import io.fundrequest.platform.profile.message.domain.MessageType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByType(MessageType type);

    List<Message> findByType(MessageType type, Sort sort);

    Message findByNameAndType(String name, MessageType type);
}
