package com.simulador.financiero.dtos.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GlobalQuoteApiResponse(
    @JsonProperty("Global Quote") GlobalQuoteDTO globalQuote,
    @JsonProperty("Note") String note,
    @JsonProperty("Information") String information,
    @JsonProperty("Error Message") String errorMessage) {
}
