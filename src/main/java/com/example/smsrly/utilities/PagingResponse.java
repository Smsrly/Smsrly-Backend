package com.example.smsrly.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagingResponse {
    @JsonProperty("page_number")
    private int pageNo;
    @JsonProperty("page_size")
    private int pageSize;
    private boolean last;
    private List<?> content;
}
