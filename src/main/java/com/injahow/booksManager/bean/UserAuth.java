package com.injahow.booksManager.bean;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserAuth {
    int userId;
    String account;
    String password;
}
