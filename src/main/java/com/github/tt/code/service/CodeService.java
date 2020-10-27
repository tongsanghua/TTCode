package com.github.tt.code.service;

import com.github.tt.code.model.MetaTable;
import com.github.tt.code.vo.TemplateVO;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public interface CodeService {

    /**
     * 验证sql语法
     *
     * @param sql
     */
    void validationCreateSql(String sql);

    /**
     * 将sql创建语句解析成表元数据
     *
     * @param sql
     */
    List<MetaTable> parseCreateSqlToTable(String sql);

    /**
     * 预览结果
     *
     * @param sql
     * @return
     */
    TemplateVO preview(String sql, Map<String, String> variable, TemplateVO template);

    /**
     * 生成代码
     *
     * @param sql
     * @param template
     * @return
     */
    ByteArrayOutputStream generate(String sql, Map<String, String> variable, List<TemplateVO> template);
}
