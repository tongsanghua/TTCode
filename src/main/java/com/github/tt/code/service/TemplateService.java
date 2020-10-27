package com.github.tt.code.service;

import com.github.tt.code.vo.TemplateVariable;
import com.github.tt.code.vo.TemplateVO;

import java.util.List;

public interface TemplateService {

    /**
     * 分页获取
     *
     * @param templateVO
     */
    List<TemplateVO> getPageList(TemplateVO templateVO);

    void add(TemplateVO templateVO);

    void delete(Integer id);

    void update(TemplateVO templateVO);

    List<TemplateVariable> getAllTemplateVariable();
}
