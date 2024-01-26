package com.devframe.global.common.hateaos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomSingleResponseBody extends CustomResponseBody {

    private BasicResponse content;

    public CustomSingleResponseBody(BasicResponse content) {
        super(true);
        this.content = content;
    }


}
