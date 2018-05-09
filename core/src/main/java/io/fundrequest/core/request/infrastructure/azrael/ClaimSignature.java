package io.fundrequest.core.request.infrastructure.azrael;

import lombok.Data;

@Data
public class ClaimSignature {

    private String platform;
    private String platformId;
    private String solver;
    private String address;
    private String r;
    private String s;
    private Integer v;
}
