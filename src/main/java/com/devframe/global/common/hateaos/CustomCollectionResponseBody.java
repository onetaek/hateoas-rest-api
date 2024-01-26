package com.devframe.global.common.hateaos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Collection;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomCollectionResponseBody<C extends Collection<? extends BasicResponse>> extends CustomResponseBody {

    private C content;
    private int size;

    public CustomCollectionResponseBody(C basicResponseCollection) {
        super(true);
        this.content = basicResponseCollection;
        this.size = basicResponseCollection.size();
    }

}
