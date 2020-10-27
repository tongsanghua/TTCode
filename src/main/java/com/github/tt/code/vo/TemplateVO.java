package com.github.tt.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateVO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private Integer id;

    private String template;

    private String content;

    private String fileType;

    private String templateName;

    private String userName;

}
