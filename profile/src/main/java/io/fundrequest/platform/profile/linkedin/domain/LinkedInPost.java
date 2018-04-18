package io.fundrequest.platform.profile.linkedin.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "linkedin_post")
@Getter
public class LinkedInPost {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "submitted_url")
    private String submittedUrl;

    @Column(name = "submitted_image_url")
    private String submittedImageUrl;

    @Column(name = "comment")
    private String comment;


}
