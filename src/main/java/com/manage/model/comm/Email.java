package com.manage.model.comm;

import lombok.Data;

/**
 * @date 2021/12/4 13:06
 */
@Data
public class Email {

    private String from;

    private String to;

    private String username;

    private String subject;

    private String content;

    private String activeUrl;
}
