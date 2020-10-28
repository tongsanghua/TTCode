package com.github.tongsanghua.code.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CodeVO {
    private String sql;

    private TemplateVO template;

    private List<TemplateVO> templateList;

    private Map<String, String> customerTemplateVariable;
}
