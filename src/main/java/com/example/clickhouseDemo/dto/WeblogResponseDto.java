package com.example.clickhouseDemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeblogResponseDto {
    @JsonProperty("ip")
    public String ip;
    @JsonProperty("timestamp")
    public String timestamp;
    @JsonProperty("method")
    public String method;
    @JsonProperty("url")
    public String url;
    @JsonProperty("protocol")
    public String protocol;
    @JsonProperty("status")
    public String status;
    @JsonProperty("size")
    public String size;
    @JsonProperty("ref")
    public String ref;
    @JsonProperty("agent")
    public String agent;
}


