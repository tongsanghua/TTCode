package com.github.tongsanghua.code.service.impl;

import com.github.tongsanghua.code.service.TemplateService;
import com.github.tongsanghua.code.vo.TemplateVariable;
import com.github.tongsanghua.code.handler.AccountHandler;
import com.github.tongsanghua.code.vo.TemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TemplateVO> getPageList(TemplateVO templateVO) {
        return jdbcTemplate.query("select id,name as templateName,content as template,file_type as fileType from template where create_user = ?", new BeanPropertyRowMapper<>(TemplateVO.class), templateVO.getUserName());
    }

    @Override
    public void add(TemplateVO templateVO) {
        jdbcTemplate.update("insert into template values(null,?,?,?,?,now(),now())", templateVO.getTemplateName(), templateVO.getFileType(), templateVO.getTemplate(), AccountHandler.getCurrentUser());
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("delete from template where id =?", id);
    }

    @Override
    public void update(TemplateVO templateVO) {
        jdbcTemplate.update("update template set content=? where id =?", templateVO.getTemplate(), templateVO.getId());
    }

    @Override
    public List<TemplateVariable> getAllTemplateVariable() {
        return jdbcTemplate.query("select * from template_variable where create_user = ?", new BeanPropertyRowMapper<>(TemplateVariable.class), AccountHandler.getCurrentUser());

    }
}
