package com.github.tt.code.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.github.tt.code.component.CodeComponent;
import com.github.tt.code.model.MetaColumn;
import com.github.tt.code.model.MetaTable;
import com.github.tt.code.service.CodeService;
import com.github.tt.code.vo.TemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CodeServiceImpl implements CodeService {

    @Autowired
    private CodeComponent codeComponent;

    @Override
    public void validationCreateSql(String sql) {
        SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
    }

    @Override
    public List<MetaTable> parseCreateSqlToTable(String sql) {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        List<MetaTable> metaTableList = new ArrayList<>();
        for (SQLStatement sqlStatement : sqlStatements) {
            if (sqlStatement instanceof MySqlCreateTableStatement) {
                MySqlCreateTableStatement mySqlCreateTableStatement = (MySqlCreateTableStatement) sqlStatement;
                List<SQLTableElement> tableElementList = mySqlCreateTableStatement.getTableElementList();
                MetaTable build = MetaTable.builder()
                        .tableName(mySqlCreateTableStatement.getName().toString().replace("`", ""))
                        .tableComment(mySqlCreateTableStatement.getComment() != null ? mySqlCreateTableStatement.getComment().toString().replace("'", "") : "")
                        .metaColumnList(new ArrayList<>())
                        .build();
                tableElementList.forEach(item -> {
                    List<MetaColumn> metaColumnList = build.getMetaColumnList();
                    if (item instanceof SQLColumnDefinition) {
                        SQLColumnDefinition sqlColumnDefinition = (SQLColumnDefinition) item;
                        MetaColumn metaColumn = MetaColumn.builder()
                                .columnName(sqlColumnDefinition.getName().toString().replace("`", ""))
                                .columnType(sqlColumnDefinition.getDataType().toString().replaceAll("\\(.*\\)", ""))
                                .columnComment(sqlColumnDefinition.getComment() != null ? sqlColumnDefinition.getComment().toString().replace("'", "") : "")
                                .build();
                        metaColumnList.add(metaColumn);
                    }
                });
                metaTableList.add(build);
            }
        }
        return metaTableList;
    }

    @Override
    public TemplateVO preview(String sql, Map<String, String> variable, TemplateVO template) {
        List<MetaTable> metaTableList = parseCreateSqlToTable(sql);
        List<String> strings = codeComponent.generateSingleTemplateResult(metaTableList,variable, template.getTemplate());
        if (CollectionUtils.isNotEmpty(strings)) {
            return TemplateVO.builder().content(strings.get(0)).fileType(template.getFileType()).build();
        }
        return TemplateVO.builder().build();
    }

    @Override
    public ByteArrayOutputStream generate(String sql, Map<String, String> variable, List<TemplateVO> template) {
        List<MetaTable> metaTableList = parseCreateSqlToTable(sql);
        return codeComponent.generateCode(metaTableList, variable,template);
    }
}
