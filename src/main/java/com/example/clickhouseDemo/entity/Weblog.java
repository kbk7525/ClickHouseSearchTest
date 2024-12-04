package com.example.clickhouseDemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Table("weblog")
@Data
public class Weblog {
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
