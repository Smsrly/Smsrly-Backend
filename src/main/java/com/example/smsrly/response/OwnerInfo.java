package com.example.smsrly.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerInfo {

    private String Name;
    private long phoneNumber;
    private String image;

}
