package com.github.tt.code.component;

import com.github.tt.code.service.TemplateService;
import com.github.tt.code.enums.JavaTypeHandlerEnum;
import com.github.tt.code.model.MetaTable;
import com.github.tt.code.vo.TemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Slf4j
public class CodeComponent {

    private VelocityEngine velocityEngine;

    @Autowired
    private TemplateService templateService;

    public List<String> generateSingleTemplateResult(List<MetaTable> metaTableList, Map<String, String> variable, String templateContent) {
        List<String> result = new ArrayList<>();
        metaTableList.forEach(item -> {
            VelocityContext context = buildContext(item, variable);
            StringWriter stringWriter = new StringWriter();
            velocityEngine.evaluate(context, stringWriter, item.getTableName(), templateContent);
            result.add(stringWriter.toString());
        });
        return result;
    }

    private VelocityContext buildContext(MetaTable metaTable, Map<String, String> variable) {
        VelocityContext context = new VelocityContext();
        context.put("TableName", firstCharToUpperCase(CaseUtils.toCamelCase(metaTable.getTableName(), false, '_')));
        context.put("tableName", CaseUtils.toCamelCase(metaTable.getTableName(), false, '_'));
        context.put("table", metaTable.getTableName());
        context.put("hasDate", false);
        List<Map<String, String>> columns = new ArrayList<>();
        metaTable.getMetaColumnList().forEach(column -> {
            Map<String, String> data = new HashMap<>();
            data.put("columnName", CaseUtils.toCamelCase(column.getColumnName(), false, '_'));
            data.put("columnComment", column.getColumnComment());
            data.put("column_name", column.getColumnName());
            JavaTypeHandlerEnum javaTypeHandlerEnum = JavaTypeHandlerEnum.getByJdbcType(column.getColumnType().toUpperCase());
            String javaType = javaTypeHandlerEnum.getJavaType();
            if (javaType.equals("Date")) {
                context.put("hasDate", true);
            }
            data.put("columnType", javaType);
            data.put("column_type", column.getColumnType());
            columns.add(data);
        });
        context.put("columns", columns);
        Set<String> strings = variable.keySet();
        strings.forEach(item -> context.put(item, variable.get(item)));
        return context;
    }


    private String firstCharToUpperCase(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return new String(new char[]{str.charAt(0)}).toUpperCase() + str.substring(1);
    }

    public ByteArrayOutputStream generateCode(List<MetaTable> metaTableList, Map<String, String> variable, List<TemplateVO> templateContents) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(byteArrayOutputStream);
        metaTableList.forEach(item -> {
            VelocityContext context = buildContext(item, variable);
            templateContents.forEach(templateContent -> {
                StringWriter stringWriter = new StringWriter();
                velocityEngine.evaluate(context, stringWriter, item.getTableName(), templateContent.getTemplate());
                try {
                    zip.putNextEntry(new ZipEntry(firstCharToUpperCase(CaseUtils.toCamelCase(item.getTableName(), false, '_')) + templateContent.getTemplateName() + "." + templateContent.getFileType()));
                    zip.write(stringWriter.toString().getBytes());
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            });

        });
        try {
            zip.close();
        } catch (IOException e) {
            log.error("流关闭异常", e);
        }
        return byteArrayOutputStream;
    }


    @PostConstruct
    public void init() {
        velocityEngine = new VelocityEngine();
        //设置模板加载路径，这里设置的是class下
        velocityEngine.setProperty(Velocity.RESOURCE_LOADER, "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //进行初始化操作
        velocityEngine.init();
    }

}
