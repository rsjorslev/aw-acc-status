package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(value = {"IsSuccess", "Message", "Timestamp"})
public class StatusResponse {

    @JsonProperty(value = "IsSuccess")
    private boolean success;

    @JsonProperty(value = "Message")
    private String message;

    @JsonProperty(value = "Timestamp")
    private long timestamp;

}
