package com.stockforum.project.controller.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostWriteRequest {
    private String title;
    private String body;
}
