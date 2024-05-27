package com.turkcell.crm.core.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityResponse {
    //rfce standarts
    private String title;
    private String detail;
    private String status;
    private String type;
}
