package com.tt.code.controller;

import com.tt.code.service.CodeService;
import com.tt.code.vo.CodeVO;
import com.tt.code.vo.ApiResult;
import com.tt.code.vo.TemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

@RestController
@Slf4j
@RequestMapping("/code")
public class CodeController {

    @Autowired
    private CodeService codeService;

    /**
     * 校验sql语法
     *
     * @param codeVO
     * @return
     */
    @PostMapping("/check-sql")
    public ApiResult checkSql(@RequestBody CodeVO codeVO) {
        try {
            codeService.validationCreateSql(codeVO.getSql());
            return ApiResult.ok(true);
        } catch (Exception e) {
            return ApiResult.error(e.getMessage());
        }

    }

    /**
     * 预览结果
     *
     * @param codeVO
     * @return
     */
    @PostMapping("/preview")
    public ApiResult preview(@RequestBody CodeVO codeVO) {
        try {
            TemplateVO templateVO = codeService.preview(codeVO.getSql(), codeVO.getCustomerTemplateVariable(), codeVO.getTemplate());
            return ApiResult.ok(templateVO);
        } catch (Exception e) {
            return ApiResult.error(e.getMessage());
        }
    }

    /**
     * 预览结果
     *
     * @param codeVO
     * @return
     */
    @PostMapping("/generate")
    public void generate(@RequestBody CodeVO codeVO, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("utf-8");
            ServletOutputStream outputStream = response.getOutputStream();
            ByteArrayOutputStream byteArrayOutputStream = codeService.generate(codeVO.getSql(), codeVO.getCustomerTemplateVariable(), codeVO.getTemplateList());
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=code.zip");
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

}
