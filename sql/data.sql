/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : tt_code

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 04/01/2021 01:13:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',
  `file_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件类型',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '模板内容',
  `create_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of template
-- ----------------------------
INSERT INTO `template` VALUES (5, 'Dao', 'java', 'package ${package}.${module}.dao;\n\nimport ${package}.${module}.vo.${TableName}VO;\nimport ${package}.${module}.po.${TableName}PO;\n\nimport java.util.List;\n\npublic interface ${TableName}Dao {\n\n${TableName}PO findById(Integer id);\n\nList<${TableName}PO> findByCondition(${TableName}VO ${tableName}VO);\n\nvoid save(${TableName}PO ${tableName}PO);\n\nint update(${TableName}PO ${tableName}PO);\n\nint deleteById(Integer id);\n\n}', 'admin', '2020-10-24 15:10:31', '2020-10-24 15:10:31');
INSERT INTO `template` VALUES (9, 'DO', 'java', 'package ${package}.${module}.po;\n\nimport lombok.Data;\n\n#if($hasDate)\nimport java.util.Date;\n#end\n\n@Data\npublic class ${TableName}PO {\n\n    #foreach($metaColumn in $columns)\n    /**\n     * $metaColumn.columnComment\n     */\n    private $metaColumn.columnType $metaColumn.columnName;\n    #end\n}\n', 'admin', '2020-10-25 00:45:47', '2020-10-25 00:45:47');
INSERT INTO `template` VALUES (10, 'Service', 'java', 'package ${package}.${module}.service;\n\nimport ${package}.${module}.vo.${TableName}VO;\nimport ${package}.${module}.po.${TableName}PO;\n\nimport java.util.List;\n\npublic interface ${TableName}Service {\n\n    ${TableName}PO findById(Integer id);\n\n    List<${TableName}PO> findByCondition(${TableName}VO ${tableName}VO);\n\n    void save(${TableName}PO ${tableName}PO);\n\n    void update(${TableName} ${tableName}PO);\n\n    void deleteById(Integer id);\n\n    List<${TableNamePO}> findAll(${TableName}VO ${tableName}VO);\n\n}', 'admin', '2020-10-25 00:47:34', '2020-10-25 00:47:34');
INSERT INTO `template` VALUES (11, 'ServiceImpl', 'java', 'package ${package}.${module}.service.impl;\n\nimport com.github.pagehelper.PageHelper;\nimport ${package}.${module}.dao.${TableName}Dao;\nimport ${package}.${module}.vo.${TableName}VO;\nimport ${package}.${module}.po.${TableName}PO;\nimport ${package}.${module}.service.${TableName}Service;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.stereotype.Service;\nimport org.springframework.transaction.annotation.Transactional;\n\nimport java.util.List;\n\n@Service\npublic class ${TableName}ServiceImpl implements ${TableName}Service {\n\n	@Autowired\n	private ${TableName}Dao ${tableName}Dao;\n\n	@Override\n	public ${TableName}PO findById(Integer id) {\n		return ${tableName}Dao.findById(id);\n	}\n\n	@Override\n	public List<${TableName}PO> findByCondition(${TableName}VO ${tableName}VO) {\n		PageHelper.startPage(${tableName}VO.getPageNum(), ${tableName}VO.getPageSize());\n		return ${tableName}Dao.findByCondition(${tableName}VO);\n	}\n\n	@Override\n	@Transactional(rollbackFor = Exception.class)\n	public void save(${TableName}PO ${tableName}PO) {\n		${tableName}Dao.save(${tableName});\n	}\n\n	@Override\n	@Transactional(rollbackFor = Exception.class)\n	public void update(${TableName}PO ${tableName}PO) {\n		${tableName}Dao.update(${tableName}PO);\n	}\n\n	@Override\n	@Transactional(rollbackFor = Exception.class)\n	public void deleteById(Integer id) {\n		${tableName}Dao.deleteById(id);\n	}\n\n	@Override\n	public List<${TableName}PO> findAll(${TableName}VO ${tableName}VO) {\n		return ${tableName}Dao.findByCondition(${tableName}VO);\n	}\n}\n', 'admin', '2020-10-25 00:49:12', '2020-10-25 00:49:12');
INSERT INTO `template` VALUES (12, 'Controller', 'java', 'package ${package}.${module}.controller;\n\nimport com.github.pagehelper.Page;\nimport ${package}.common.vo.Result;\nimport ${package}.${module}.vo.${TableName}VO;\nimport ${package}.${module}.po.${TableName}PO;\nimport ${package}.${module}.service.${TableName}Service;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport lombok.extern.slf4j.Slf4j;\nimport org.springframework.web.bind.annotation.*;\n\nimport java.util.List;\n\n@RestController\n@Slf4j\n@RequestMapping(\"/${module}/${tableName}\")\npublic class ${TableName}Controller {\n\n    @Autowired\n    private ${TableName}Service ${tableName}Service;\n\n    @GetMapping(\"/list\")\n    public Result getPageList(${TableName}VO ${tableName}VO) {\n        try {\n            List<${TableName}PO> ${tableName}List = ${tableName}Service.findByCondition(${tableName}VO);\n            if (${className}List instanceof Page) {\n                Page page = (Page) ${tableName}List;\n                return Result.ok(${tableName}List, page.getPageNum(), page.getPageSize(), (int) page.getTotal());\n            }\n            return Result.ok(${tableName}List);\n        } catch (Exception e) {\n            log.error(e.getMessage(), e);\n            return Result.error(e.getMessage());\n        }\n    }\n\n    @GetMapping(\"/all-list\")\n    public Result getAllList(${TableName}VO ${tableName}VO) {\n        try {\n            return Result.ok(${tableName}Service.findAll(${tableName}VO));\n        } catch (Exception e) {\n            log.error(e.getMessage(), e);\n            return Result.error(e.getMessage());\n        }\n    }\n\n    @PostMapping(\"/save\")\n    public Result save(@RequestBody ${TableName}PO ${tableName}PO) {\n        try {\n            ${tableName}Service.save(${tableName}PO);\n            return Result.ok();\n        } catch (Exception e) {\n            log.error(e.getMessage(), e);\n            return Result.error(e.getMessage());\n        }\n    }\n\n    @RequestMapping(\"/update\")\n    public Result update(@RequestBody ${TableName}PO ${tableName}PO) {\n        try {\n            ${tableName}Service.update(${tableName}PO);\n            return Result.ok();\n        } catch (Exception e) {\n            log.error(e.getMessage(), e);\n            return Result.error(e.getMessage());\n        }\n    }\n\n    @PostMapping(\"/delete/{id}\")\n    public Result remove(@PathVariable Integer id) {\n        try {\n            ${tableName}Service.deleteById(id);\n            return Result.ok();\n        } catch (Exception e) {\n            log.error(e.getMessage(), e);\n            return Result.error(e.getMessage());\n        }\n    }\n}\n', 'admin', '2020-10-25 00:50:50', '2020-10-25 00:50:50');
INSERT INTO `template` VALUES (13, 'Mapper', 'xml', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n<mapper namespace=\"${package}.${module}.dao.${TableName}Dao\">\n\n	<select id=\"findById\" parameterType=\"java.lang.Integer\" resultType=\"${package}.${module}.po.${TableName}PO\">\n		select *\n		from ${table}\n		where id = #{id}\n	</select>\n\n	<select id=\"findByCondition\" parameterType=\"${package}.${module}.vo.${TableName}VO\" resultType=\"${package}.${module}.po.${TableName}PO\">\n		select *\n		from\n		${table}\n        <where>\n        #foreach($metaColumn in $columns)\n		#if($metaColumn.columnName!=\"id\" && $metaColumn.columnType!=\"Date\")\n        <if test=\"$metaColumn.columnName != null #if($metaColumn.columnType==\"String\") and $metaColumn.columnName != \'\' #end\">\n            and `$metaColumn.column_name` = #{$metaColumn.columnName}\n        </if>\n		#end\n        #end\n		</where>\n	</select>\n\n	<insert id=\"save\" parameterType=\"${package}.${module}.model.${TableName}\">\n		<selectKey keyProperty=\"id\" order=\"AFTER\" resultType=\"java.lang.Integer\">\n			select last_insert_id()\n		</selectKey>\n		insert into ${table}\n		<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n            #foreach($metaColumn in $columns)\n			#if(($metaColumn.columnName == \"createTime\"))\n				`create_time`,\n			#elseif(($metaColumn.columnName == \"updateTime\"))\n				`update_time`,\n            #elseif($metaColumn.columnName != \"id\")\n			<if test=\"$metaColumn.columnName != null\">\n				`$metaColumn.column_name`,\n			</if>\n			#end\n            #end\n		</trim>\n		<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n            #foreach($metaColumn in $columns)\n                #if(($metaColumn.columnName == \"createTime\" || $metaColumn.columnName == \"updateTime\")&&$metaColumn.columnName != \"id\")\n                now(),\n                #elseif($metaColumn.columnName != \"id\")\n                <if test=\"$metaColumn.columnName != null\">\n                #{$metaColumn.columnName},\n                </if>\n                #end\n            #end\n		</trim>\n	</insert>\n\n	<update id=\"update\" parameterType=\"${package}.${module}.model.${TableName}\">\n		update ${table}\n		<set>\n		    #foreach($metaColumn in $columns)\n		        #if($metaColumn.columnName == \'updateTime\'&&$metaColumn.columnName != \'id\')\n                `$metaColumn.column_name` = now(),\n                #elseif($metaColumn.columnName != \'id\'&& $metaColumn.columnName != \'createTime\')\n			    <if test=\"$metaColumn.columnName != null\">`$metaColumn.column_name` = #{$metaColumn.columnName}, </if>\n                #end\n            #end\n		</set>\n		where id = #{id}\n	</update>\n\n	<delete id=\"deleteById\" parameterType=\"java.lang.Integer\">\n		delete from ${table} where id = #{id}\n	</delete>\n	\n</mapper>', 'admin', '2020-10-25 01:04:16', '2020-10-25 01:04:16');
INSERT INTO `template` VALUES (14, 'VO', 'java', 'package ${package}.${module}.vo;\n\nimport lombok.Data;\n\n@Data\npublic class ${TableName}VO {\n\n    private Integer pageNum = 1;\n\n    private Integer pageSize = 10;\n\n    #foreach($metaColumn in $columns)\n    #if($metaColumn.columnName!=\"id\" && $metaColumn.columnType!=\"Date\")\n    /**\n     * $metaColumn.columnComment\n     */\n    private $metaColumn.columnType $metaColumn.columnName;\n    #end\n    #end\n}\n', 'admin', '2020-10-25 21:56:06', '2020-10-25 21:56:06');
INSERT INTO `template` VALUES (15, 'Vue', 'vue', '<template>\n  <div class=\"app-container\">\n    <div class=\"filter-container\">\n      #foreach($metaColumn in $columns)\n      #if($metaColumn.columnName!=\"id\" && $metaColumn.columnType!=\"Date\")\n      <el-input v-model=\"queryParam.$metaColumn.columnName\" placeholder=\"$metaColumn.columnComment\" style=\"width: 200px;\" class=\"filter-item\" />\n      #end\n      #end\n      <el-button class=\"filter-item\" style=\"margin-left: 10px;\" type=\"primary\" icon=\"el-icon-search\" @click=\"search\">\n        搜索\n      </el-button>\n      <el-button\n        class=\"filter-item\"\n        style=\"margin-left: 10px;\"\n        type=\"primary\"\n        icon=\"el-icon-edit\"\n        @click=\"createDialogShow = true\"\n      >添加\n      </el-button>\n    </div>\n\n    <el-table\n      v-loading=\"loading\"\n      border\n      fit\n      :data=\"tableData\"\n      row-key=\"id\"\n      highlight-current-row\n      style=\"width: 100%;\"\n    >\n      >\n      #foreach($metaColumn in $columns)\n      <el-table-metaColumn label=\"$metaColumn.columnComment\" prop=\"$metaColumn.columnName\" align=\"center\" />\n      #end\n      <el-table-metaColumn label=\"操作\" align=\"center\" class-name=\"small-padding fixed-width\">\n        <template slot-scope=\"{row}\">\n          <el-button type=\"primary\" @click=\"edit(row)\">编辑</el-button>\n          <el-button type=\"danger\" @click=\"remove(row)\">删除</el-button>\n        </template>\n      </el-table-metaColumn>\n    </el-table>\n\n    <el-pagination :page-size=\"queryParam.pageSize\" :current-page=\"queryParam.pageNum\" :total=\"queryParam.total\" @current-change=\"pageChange\"/>\n\n    <el-dialog :visible.sync=\"createDialogShow\" :close-on-click-modal=\"false\" destroy-on-close @close=\"closeDialogCallBack\">\n      <el-form ref=\"form\" :model=\"form\" label-position=\"right\" label-width=\"100px\">\n        #foreach($metaColumn in $columns)\n        #if($metaColumn.columnName!=\"id\" && $metaColumn.columnType!=\"Date\")\n        <el-form-item label=\"$metaColumn.columnComment\">\n          <el-input v-model=\"form.$metaColumn.columnName\" />\n        </el-form-item>\n        #end\n        #end\n      </el-form>\n      <div slot=\"footer\" class=\"dialog-footer\">\n        <el-button type=\"primary\" @click=\"save\">提交</el-button>\n      </div>\n    </el-dialog>\n  </div>\n</template>\n\n<script>\nimport { list, remove, add, update } from \'../../../api/${module}/${tableName}\'\n\nexport default {\n  data() {\n    return {\n      tableData: [],\n      loading: false,\n      queryParam: {\n        #foreach($metaColumn in $columns)\n        #if($metaColumn.columnName!=\"id\" && $metaColumn.columnType!=\"Date\")\n        $metaColumn.columnName:\'\',\n        #end\n        #end\n        pageNum: 1,\n        pageSize: 10,\n        total: 0\n      },\n      createDialogShow: false,\n      form: {\n        #foreach($metaColumn in $columns)\n        #if($metaColumn.columnName!=\"id\" && $metaColumn.columnType!=\"Date\")\n        $metaColumn.columnName:\'\',\n        #end\n        #end\n      }\n    }\n  },\n  created() {\n    this.getTableData()\n  },\n  methods: {\n    getTableData() {\n      this.loading = true\n      list(this.queryParam).then(res => {\n        this.loading = false\n        if (res.data.status === 200) {\n          this.tableData = res.data.data\n          this.queryParam.total = res.data.total\n        }\n      }).catch(error => {\n        this.loading = false\n        console.error(error)\n      })\n    },\n    search() {\n      this.queryParam.pageNum = 1\n      this.getTableData()\n    },\n    edit(row) {\n      this.form = { ...row }\n      this.createDialogShow = true\n    },\n    async remove(row) {\n      try {\n        const confirm = await this.$confirm(\'删除\' + row.id + \', 是否继续?\', \'提示\', {\n          confirmButtonText: \'确定\',\n          cancelButtonText: \'取消\',\n          showCancelButton: false,\n          type: \'warning\'\n        })\n        if (confirm === \'confirm\') {\n          let loading\n          try {\n            loading = this.$loading({ fullscreen: true })\n            const res = await remove(row)\n            loading.close()\n            if (res.data.status === 200) {\n              this.getTableData()\n            }\n          } catch (e) {\n            console.error(e)\n            loading.close()\n            this.$message({\n              type: \'error\',\n              message: \'删除失败\'\n            })\n          }\n        }\n      } catch (e) {\n        console.log(\'取消删除\')\n      }\n    },\n    async save() {\n      const loading = this.$loading({ fullscreen: true })\n      let res\n      try {\n        if (this.form.id) {\n          res = await update(this.form)\n        } else {\n          res = await add(this.form)\n        }\n        loading.close()\n        if (res.data.status === 200) {\n          this.$message({ type: \'success\', message: this.form.id ? \'更新成功\' : \'添加成功\' })\n          this.createDialogShow = false\n          this.getTableData()\n        }\n      } catch (e) {\n        console.error(e)\n        loading.close()\n        this.$message({ type: \'error\', message: \'提交\' })\n      }\n    },\n    pageChange(page) {\n      this.queryParam.pageNum = page\n      this.getTableData()\n    },\n    closeDialogCallBack() {\n      this.form.id = null\n    }\n  }\n}\n</script>\n\n<style lang=\"scss\">\n  .el-table .cell {\n    white-space: nowrap;\n  }\n</style>\n', 'admin', '2020-10-25 22:00:09', '2020-10-25 22:00:09');

SET FOREIGN_KEY_CHECKS = 1;
