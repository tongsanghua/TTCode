package com.github.tt.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateVariable {


    private Integer id;

    private String name;

    private String description;

    private String value;

}
