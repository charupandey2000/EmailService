package dev.charu.emailservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendEmailDto {
    private String to;
    private String from;
    private  String Subject;
    private String Body;

}
