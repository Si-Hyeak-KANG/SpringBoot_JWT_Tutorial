package me.james.tutorial.dto;

import lombok.*;

// Response 시 전달할 Token info
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class TokenDto {

    private String token;
}
