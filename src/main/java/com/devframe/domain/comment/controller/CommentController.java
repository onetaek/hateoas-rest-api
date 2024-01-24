package com.devframe.domain.comment.controller;

import com.devframe.domain.comment.service.CommentCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentCommandService childService;

}
