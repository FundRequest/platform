package io.fundrequest.core.request.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTechnology {

    @Column(name = "technology")
    private String technology;

    @Column(name = "weight")
    private Long weight;
}
