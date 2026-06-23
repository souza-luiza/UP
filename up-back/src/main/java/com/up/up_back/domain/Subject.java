package com.up.up_back.domain;

import lombok.Builder;

@Builder
public record Subject(

        String name,

        int difficulty
) {

}
