package com.github.tongsanghua.code.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;

@Configuration
@Slf4j
public class CodeConfig {

    @Value("${code.auto.create}")
    private boolean disable;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        if (disable) {
            log.info("开始建表");
            Boolean execute = jdbcTemplate.execute((ConnectionCallback<Boolean>) connection -> {
                ResultSet rs = connection.getMetaData().getTables(null, null, "template", null);
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            });
            if (execute != null && execute) {
                log.info("表已存在，无需建表");
            } else {
                String[] sql = new String[]{
                        "CREATE TABLE `template`  (\n" +
                                "  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                                "  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',\n" +
                                "  `file_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件类型',\n" +
                                "  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '模板内容',\n" +
                                "  `create_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',\n" +
                                "  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                                "  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',\n" +
                                "  PRIMARY KEY (`id`) USING BTREE\n" +
                                ") ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COMMENT '模板表'",
                        "INSERT INTO `template` VALUES (1,'Dao','java','package ${package}.${module}.dao;\\n\\nimport ${package}.${module}.vo.${TableName}VO;\\nimport ${package}.${module}.po.${TableName}PO;\\n\\nimport java.util.List;\\n\\npublic interface ${TableName}Dao {\\n\\n\t ${TableName}PO findById(Integer id);\\n\\n    List<${TableName}PO> findByCondition(${TableName}VO ${tableName});\\n\\n    void save(${TableName}PO ${tableName});\\n\\n    int update(${TableName}PO ${tableName});\\n\\n    int deleteById(Integer id);\\n\\n}','admin','2020-10-24 15:10:31','2020-10-24 15:10:31'),(2,'PO','java','package ${package}.${module}.po;\\n\\nimport lombok.Data;\\nimport lombok.AllArgsConstructor;\\nimport lombok.Builder;\\nimport lombok.NoArgsConstructor;\\n\\n#if($hasDate)\\nimport java.util.Date;\\n#end\\n\\n@Data\\n@Builder\\n@AllArgsConstructor\\n@NoArgsConstructor\\npublic class ${TableName}PO {\\n\\n    #foreach($metaColumn in $columns)\\n    /**\\n     * $metaColumn.columnComment\\n     */\\n    private $metaColumn.columnType $metaColumn.columnName;\\n    #end\\n}\\n','admin','2020-10-25 00:45:47','2020-10-25 00:45:47'),(3,'Service','java','package ${package}.${module}.service;\\n\\nimport ${package}.${module}.vo.${TableName}VO;\\nimport ${package}.${module}.po.${TableName}PO;\\n\\nimport java.util.List;\\n\\npublic interface ${TableName}Service {\\n\\n    ${TableName}PO findById(Integer id);\\n\\n    List<${TableName}PO> findByCondition(${TableName}VO ${tableName});\\n\\n    void save(${TableName}PO ${tableName});\\n\\n    void update(${TableName}PO ${tableName});\\n\\n    void deleteById(Integer id);\\n\\n    List<${TableName}PO> findAll(${TableName}VO ${tableName});\\n\\n}','admin','2020-10-25 00:47:34','2020-10-25 00:47:34'),(4,'ServiceImpl','java','package ${package}.${module}.service.impl;\\n\\nimport com.github.pagehelper.PageHelper;\\nimport ${package}.${module}.dao.${TableName}Dao;\\nimport ${package}.${module}.vo.${TableName}VO;\\nimport ${package}.${module}.po.${TableName}PO;\\nimport ${package}.${module}.service.${TableName}Service;\\nimport org.springframework.beans.factory.annotation.Autowired;\\nimport org.springframework.stereotype.Service;\\nimport org.springframework.transaction.annotation.Transactional;\\n\\nimport java.util.List;\\n\\n@Service\\npublic class ${TableName}ServiceImpl implements ${TableName}Service {\\n\\n\t@Autowired\\n\tprivate ${TableName}Dao ${tableName}Dao;\\n\\n\t@Override\\n\tpublic ${TableName}PO findById(Integer id) {\\n\t\treturn ${tableName}Dao.findById(id);\\n\t}\\n\\n\t@Override\\n\tpublic List<${TableName}PO> findByCondition(${TableName}VO ${tableName}) {\\n\t\tPageHelper.startPage(${tableName}.getPageNum(), ${tableName}.getPageSize());\\n\t\treturn ${tableName}Dao.findByCondition(${tableName});\\n\t}\\n\\n\t@Override\\n\t@Transactional(rollbackFor = Exception.class)\\n\tpublic void save(${TableName}PO ${tableName}) {\\n\t\t${tableName}Dao.save(${tableName});\\n\t}\\n\\n\t@Override\\n\t@Transactional(rollbackFor = Exception.class)\\n\tpublic void update(${TableName}PO ${tableName}) {\\n\t\t${tableName}Dao.update(${tableName});\\n\t}\\n\\n\t@Override\\n\t@Transactional(rollbackFor = Exception.class)\\n\tpublic void deleteById(Integer id) {\\n\t\t${tableName}Dao.deleteById(id);\\n\t}\\n\\n\t@Override\\n\tpublic List<${TableName}PO> findAll(${TableName}VO ${tableName}) {\\n\t\treturn ${tableName}Dao.findByCondition(${tableName});\\n\t}\\n}\\n','admin','2020-10-25 00:49:12','2020-10-25 00:49:12'),(5,'Controller','java','package ${package}.${module}.controller;\\n\\nimport com.github.pagehelper.Page;\\nimport ${package}.common.vo.Result;\\nimport ${package}.${module}.vo.${TableName}VO;\\nimport ${package}.${module}.po.${TableName}PO;\\nimport ${package}.${module}.service.${TableName}Service;\\nimport org.springframework.beans.factory.annotation.Autowired;\\nimport lombok.extern.slf4j.Slf4j;\\nimport org.springframework.web.bind.annotation.*;\\n\\nimport java.util.List;\\n\\n@RestController\\n@Slf4j\\n@RequestMapping(\\\"/${module}/${tableName}\\\")\\npublic class ${TableName}Controller {\\n\\n    @Autowired\\n    private ${TableName}Service ${tableName}Service;\\n\\n    @GetMapping(\\\"/list\\\")\\n    public Result getPageList(${TableName}VO ${tableName}) {\\n        try {\\n            List<${TableName}PO> ${tableName}List = ${tableName}Service.findByCondition(${tableName});\\n            if (${tableName}List instanceof Page) {\\n                Page page = (Page) ${tableName}List;\\n                return Result.ok(${tableName}List, page.getPageNum(), page.getPageSize(), (int) page.getTotal());\\n            }\\n            return Result.ok(${tableName}List);\\n        } catch (Exception e) {\\n            log.error(e.getMessage(), e);\\n            return Result.error(e.getMessage());\\n        }\\n    }\\n\\n    @GetMapping(\\\"/all-list\\\")\\n    public Result getAllList(${TableName}VO ${tableName}) {\\n        try {\\n            return Result.ok(${tableName}Service.findAll(${tableName}));\\n        } catch (Exception e) {\\n            log.error(e.getMessage(), e);\\n            return Result.error(e.getMessage());\\n        }\\n    }\\n\\n    @PostMapping(\\\"/save\\\")\\n    public Result save(@RequestBody ${TableName}PO ${tableName}) {\\n        try {\\n            ${tableName}Service.save(${tableName});\\n            return Result.ok();\\n        } catch (Exception e) {\\n            log.error(e.getMessage(), e);\\n            return Result.error(e.getMessage());\\n        }\\n    }\\n\\n    @PostMapping(\\\"/update\\\")\\n    public Result update(@RequestBody ${TableName}PO ${tableName}) {\\n        try {\\n            ${tableName}Service.update(${tableName});\\n            return Result.ok();\\n        } catch (Exception e) {\\n            log.error(e.getMessage(), e);\\n            return Result.error(e.getMessage());\\n        }\\n    }\\n\\n    @PostMapping(\\\"/delete/{id}\\\")\\n    public Result remove(@PathVariable Integer id) {\\n        try {\\n            ${tableName}Service.deleteById(id);\\n            return Result.ok();\\n        } catch (Exception e) {\\n            log.error(e.getMessage(), e);\\n            return Result.error(e.getMessage());\\n        }\\n    }\\n}\\n','admin','2020-10-25 00:50:50','2020-10-25 00:50:50'),(6,'Mapper','xml','<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n<!DOCTYPE mapper PUBLIC \\\"-//mybatis.org//DTD Mapper 3.0//EN\\\" \\\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\\\">\\n\\n<mapper namespace=\\\"${package}.${module}.dao.${TableName}Dao\\\">\\n\\n\t<select id=\\\"findById\\\" parameterType=\\\"java.lang.Integer\\\" resultType=\\\"${package}.${module}.po.${TableName}PO\\\">\\n\t\tselect *\\n\t\tfrom ${table}\\n\t\twhere id = #{id}\\n\t</select>\\n\\n\t<select id=\\\"findByCondition\\\" parameterType=\\\"${package}.${module}.vo.${TableName}VO\\\" resultType=\\\"${package}.${module}.po.${TableName}PO\\\">\\n\t\tselect *\\n\t\tfrom\\n\t\t${table}\\n     <where>\\n     #foreach($metaColumn in $columns)\\n     #if($metaColumn.columnName!=\\\"id\\\" && $metaColumn.columnType!=\\\"Date\\\")\\n       <if test=\\\"$metaColumn.columnName != null #if($metaColumn.columnType==\\\"String\\\") and $metaColumn.columnName != \\'\\' #end\\\">\\n       \t\tand `$metaColumn.column_name` = #{$metaColumn.columnName}\\n       </if>\\n     #end\\n     #end\\n\t\t</where>\\n\t</select>\\n\\n\t<insert id=\\\"save\\\" parameterType=\\\"${package}.${module}.po.${TableName}PO\\\">\\n\t\t<selectKey keyProperty=\\\"id\\\" order=\\\"AFTER\\\" resultType=\\\"java.lang.Integer\\\">\\n\t\t\tselect last_insert_id()\\n\t\t</selectKey>\\n\t\tinsert into ${table}\\n\t\t<trim prefix=\\\"(\\\" suffix=\\\")\\\" suffixOverrides=\\\",\\\">\\n        #foreach($metaColumn in $columns)\\n        #if(($metaColumn.columnName == \\\"createTime\\\"))\\n        `create_time`,\\n        #elseif(($metaColumn.columnName == \\\"updateTime\\\"))\\n        `update_time`,\\n        #elseif($metaColumn.columnName != \\\"id\\\")\\n        <if test=\\\"$metaColumn.columnName != null\\\">\\n        `$metaColumn.column_name`,\\n        </if>\\n        #end\\n        #end\\n\t\t</trim>\\n\t\t<trim prefix=\\\"values (\\\" suffix=\\\")\\\" suffixOverrides=\\\",\\\">\\n        #foreach($metaColumn in $columns)\\n        #if(($metaColumn.columnName == \\\"createTime\\\" || $metaColumn.columnName == \\\"updateTime\\\")&&$metaColumn.columnName != \\\"id\\\")\\n         \tnow(),\\n        #elseif($metaColumn.columnName != \\\"id\\\")\\n        <if test=\\\"$metaColumn.columnName != null\\\">\\n         \t#{$metaColumn.columnName},\\n        </if>\\n        #end\\n        #end\\n\t\t</trim>\\n\t</insert>\\n\\n\t<update id=\\\"update\\\" parameterType=\\\"${package}.${module}.po.${TableName}PO\\\">\\n\t\tupdate ${table}\\n\t\t<set>\\n        #foreach($metaColumn in $columns)\\n        #if($metaColumn.columnName == \\'updateTime\\'&&$metaColumn.columnName != \\'id\\')\\n        `$metaColumn.column_name` = now(),\\n        #elseif($metaColumn.columnName != \\'id\\'&& $metaColumn.columnName != \\'createTime\\')\\n        <if test=\\\"$metaColumn.columnName != null\\\">`$metaColumn.column_name` = #{$metaColumn.columnName}, </if>\\n        #end\\n        #end\\n\t\t</set>\\n\t\twhere id = #{id}\\n\t</update>\\n\\n\t<delete id=\\\"deleteById\\\" parameterType=\\\"java.lang.Integer\\\">\\n\t\tdelete from ${table} where id = #{id}\\n\t</delete>\\n\t\\n</mapper>','admin','2020-10-25 01:04:16','2020-10-25 01:04:16'),(7,'VO','java','package ${package}.${module}.vo;\\n\\nimport lombok.Data;\\nimport lombok.AllArgsConstructor;\\nimport lombok.Builder;\\nimport lombok.NoArgsConstructor;\\n\\n@Data\\n@Builder\\n@AllArgsConstructor\\n@NoArgsConstructor\\npublic class ${TableName}VO {\\n\\n    private Integer pageNum = 1;\\n\\n    private Integer pageSize = 10;\\n\\n    #foreach($metaColumn in $columns)\\n    #if($metaColumn.columnName!=\\\"id\\\" && $metaColumn.columnType!=\\\"Date\\\")\\n    /**\\n     * $metaColumn.columnComment\\n     */\\n    private $metaColumn.columnType $metaColumn.columnName;\\n    #end\\n    #end\\n}\\n','admin','2020-10-25 21:56:06','2020-10-25 21:56:06'),(8,'React','js','import React, { useState, useEffect, useRef } from \\'react\\'\\n\\nimport { Button, Divider, Input, Table, Form, Modal, notification } from \\'antd\\'\\n\\nexport default() => {\\n  const [tableData, setTableData] = useState([])\\n  const [query, setQuery] = useState({\\n    pageNum: 1,\\n    pageSize: 10,\\n    total: 0,\\n    fetch: false,\\n    #foreach($metaColumn in $columns)\\n    #if($metaColumn.columnName!=\\\"id\\\" && $metaColumn.columnType!=\\\"Date\\\")\\n    $metaColumn.columnName:\\'\\',\\n    #end\\n    #end\\n  })\\n  const [tableLoading, setTableLoading] = useState(false)\\n  const [formModalShow, setFormModalShow] = useState(false)\\n  const formInstance = useRef(null)\\n  const [formModalSubmitButtonLoading, setFormModalSubmitButtonLoading] = useState(false)\\n  useEffect(() => {\\n    getTableData()\\n  }, [])\\n\\n  useEffect(() => {\\n    if (query.fetch) {\\n      getTableData()\\n    }\\n  }, [query])\\n  const getTableData = () => {\\n    let url = \\'/${module}/${tableName}/list\\'\\n    if (query) {\\n        let paramsArray = [];\\n        Object.keys(query).forEach(key => paramsArray.push(key + \\'=\\' + query[key]))\\n        if (url.search(/\\\\?/) === -1) {\\n            url += \\'?\\' + paramsArray.join(\\'&\\')\\n        } else {\\n            url += \\'&\\' + paramsArray.join(\\'&\\')\\n        }\\n    }\\n    setTableLoading(true)\\n    fetch(url)\\n    .then(res=>res.json())\\n    .then(res => {\\n      if(res.data.status === 200){\\n          setTableData(res.data.data)\\n          setQuery({...query, total:res.data.total, fetch:false})\\n      } else{\\n          notification.error({ message: res.data.msg, duration: 3 })\\n      }\\n    }).catch(error => {\\n      console.error(error)\\n    }).finally(()=>{\\n      setTableLoading(false)\\n    })\\n  }\\n\\n  const save = (data) => {\\n    setFormModalSubmitButtonLoading(true)\\n    if (!data.id) {\\n      fetch(\\'/${module}/${tableName}/save\\',{\\n            method:\\'POST\\',\\n            headers:{\\n                \\'Content-Type\\':\\'application/json\\',\\n            },\\n            body:JSON.stringify({ ...data }),\\n       })\\n      .then(res=>res.json())\\n      .then(res => {\\n        if(res.data.status===200){\\n            setFormModalShow(false)\\n            notification.success({ message: \\'添加成功\\', duration: 3 })\\n            setQuery({ ...query, pageNum: 1, fetch: true })\\n        } else{\\n            notification.error({ message: res.data.msg, duration: 3 })\\n        }\\n      }).finally(()=>{\\n\t\tsetFormModalSubmitButtonLoading(false)\\n      })\\n    } else {\\n      fetch(\\'/${module}/${tableName}/update\\',{\\n            method:\\'POST\\',\\n            headers:{\\n                \\'Content-Type\\':\\'application/json\\',\\n            },\\n            body:JSON.stringify({ ...data }),\\n       })\\n      .then(res=>res.json())\\n      .then(res => {\\n        if(res.data.status===200){\\n            setFormModalShow(false)\\n            notification.success({ message: \\'修改成功\\', duration: 3 })\\n            setQuery({ ...query, pageNum: 1, fetch: true })\\n        } else{\\n            notification.error({ message: res.data.msg, duration: 3 })\\n        }\\n      }).finally(()=>{\\n\t\tsetFormModalSubmitButtonLoading(false)\\n      })\\n    }\\n  }\\n  const deleteData = (data) => {\\n    Modal.confirm({\\n      title: \\'删除\\',\\n      content: `确认删除${data.id} ?`,\\n      okText: \\'确认\\',\\n      cancelText: \\'取消\\',\\n      onOk: () => {\\n        fetch(`/${module}/${tableName}/delete/${data.id}`,{\\n                method:\\'POST\\',\\n                headers:{\\n                    \\'Content-Type\\':\\'application/json\\',\\n                },\\n                body:JSON.stringify(data),\\n        })\\n        .then(res=>res.json())\\n        .then(res => {\\n            if(res.data.status===200){\\n                notification.success({ message: \\'删除成功\\', duration: 3 })\\n                setQuery({ ...query, fetch: true })\\n            } else{\\n                notification.error({ message: res.data.msg, duration: 3 })\\n            }\\n        })\\n      }\\n    })\\n  }\\n\\n  const columns = [ \\n    #foreach($metaColumn in $columns)\\n    {\\n        title: \\'$metaColumn.columnComment\\',\\n        align: \\'center\\',\\n        dataIndex:\\'$metaColumn.columnName\\',\\n        key:\\'$metaColumn.columnName\\'\\n    },\\n    #end\\n    {\\n    title: \\'操作\\',\\n    align: \\'center\\',\\n    width: \\'20%\\',\\n    render: (value, record) => {\\n      return <div>\\n        <Divider type=\\'vertical\\' />\\n        <Button type=\\'primary\\' onClick={() => {\\n          setFormModalShow(true)\\n          formInstance.current.setFieldsValue({ ...record })\\n        }}>编辑</Button>\\n        <Divider type=\\'vertical\\' />\\n        <Button type=\\'danger\\' onClick={() => {\\n          deleteData(record)\\n        }}>删除</Button>\\n      </div>\\n    }\\n  }]\\n  const getFieldValue = key => {\\n    return formInstance.current && formInstance.current.getFieldValue(key)\\n  }\\n  const renderFormModal = () => {\\n    return <Modal\\n      title={getFieldValue(\\'id\\') ? \\'修改\\' : \\'添加\\'}\\n      visible={formModalShow}\\n      forceRender\\n      okText={\\'提交\\'}\\n      cancelText={\\'取消\\'}\\n      footer={<div>\\n        <Button onClick={() => {\\n          setFormModalShow(false)\\n        }}>取消</Button>\\n        <Button loading={formModalSubmitButtonLoading} type=\\'primary\\' onClick={() => {\\n          formInstance.current.validateFields()\\n            .then(values => {\\n              // 表单校验成功\\n              save(formInstance.current.getFieldValue())\\n            }).catch(e => {\\n              // 表单校验失败\\n              console.log(e)\\n            })\\n        }}>提交</Button>\\n      </div>}\\n      onCancel={() => {\\n        setFormModalShow(false)\\n      }}\\n    >\\n      <Form ref={formInstance} labelCol={{ span: 4 }} wrapperCol={{ span: 16 }}>\\n        #foreach($metaColumn in $columns)\\n        #if($metaColumn.columnName!=\\\"id\\\" && $metaColumn.columnType!=\\\"Date\\\")\\n        <Form.Item label=\\'$metaColumn.columnComment\\' name=\\'$metaColumn.columnName\\' rules={[{ required: true, message: \\'必填\\' }]} hasFeedbac>\\n          <Input/>\\n        </Form.Item>\\n        #end\\n        #end\\n      </Form>\\n    </Modal>\\n  }\\n  return <div className=\\'${tableName}-contain\\'>\\n    <div className=\\'${tableName}-contain-search\\' style={{ marginBottom: 10 }}>\\n      <Form name=\\'horizontal_login\\' layout=\\'inline\\'>\\n        #foreach($metaColumn in $columns)\\n        #if($metaColumn.columnName!=\\\"id\\\" && $metaColumn.columnType!=\\\"Date\\\")\\n        <Form.Item>\\n          <Input value={query.$metaColumn.columnName} onChange={e => { setQuery({ ...query, $metaColumn.columnName: e.target.value, fetch: false }) }} placeholder=\\'$metaColumn.columnComment\\' onPressEnter={() => setQuery({ ...query, \t\tpageNum: 1, fetch: true })} />\\n        </Form.Item>\\n        #end\\n        #end\\n\\n        <Form.Item >\\n          <Button type=\\'primary\\' onClick={() => { setQuery({ ...query, pageNum: 1, fetch: true }) }} >查询</Button>\\n        </Form.Item>\\n        <Form.Item >\\n          <Button type=\\'primary\\' onClick={() => {\\n            formInstance.current.setFieldsValue({ id: null, parentId: 0 })\\n            setFormModalShow(true)\\n          }} >添加</Button>\\n        </Form.Item>\\n      </Form>\\n    </div>\\n    <div className=\\'${tableName}-contain-content\\'>\\n      <Table\\n        rowKey=\\'id\\'\\n        loading={tableLoading}\\n        dataSource={tableData}\\n        columns={columns}\\n        pagination={{\\n          current: query.pageNum,\\n          pageSize: query.pageSize,\\n          total: query.total,\\n          showSizeChanger: false,\\n          onChange: page => {\\n            setQuery({ ...query, pageNum: page, fetch: true })\\n          }\\n        }} />\\n    </div>\\n    {renderFormModal()}\\n  </div>\\n}\\n','admin','2020-10-31 11:49:26','2020-10-31 11:49:26');\n"
                };
                jdbcTemplate.batchUpdate(sql);
                log.info("建表完成");
            }
        }

    }

}
