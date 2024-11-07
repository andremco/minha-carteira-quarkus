package org.finance.models.dto.integration.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetQuoteResponse {
    private List<GetQuote> results;
    private String requestedAt;
    private String took;
}
