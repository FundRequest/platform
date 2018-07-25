package io.fundrequest.core.message.domain;

import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "messages")
@Getter
public class Message extends AbstractEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(name = "title")
    private String title;


    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    Message() {
    }

    @Builder
    public Message(String name, MessageType type, String title, String description, String link) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.link = link;
        this.type = type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

}