package com.github.tt.code.controller;

import com.github.tt.code.service.TemplateService;
import com.github.tt.code.vo.ApiResult;
import com.github.tt.code.constants.UserConstants;
import com.github.tt.code.handler.AccountHandler;
import com.github.tt.code.vo.TemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/code/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping("/list")
    public ApiResult pageList(TemplateVO templateVO) {
        try {
            templateVO.setUserName(AccountHandler.getCurrentUser());
            return ApiResult.ok(templateService.getPageList(templateVO));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ApiResult.error(e.getMessage());
        }
    }

    @GetMapping("/default-list")
    public ApiResult defaultList(TemplateVO templateVO) {
        try {
            templateVO.setUserName(UserConstants.ADMIN_USER);
            return ApiResult.ok(templateService.getPageList(templateVO));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ApiResult.error(e.getMessage());
        }
    }

    @PostMapping("")
    public ApiResult add(@RequestBody TemplateVO templateVO) {
        try {
            templateService.add(templateVO);
            return ApiResult.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ApiResult.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResult update(@PathVariable Integer id, @RequestBody TemplateVO templateVO) {
        try {
            templateVO.setId(id);
            templateService.update(templateVO);
            return ApiResult.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ApiResult.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Integer id) {
        try {
            templateService.delete(id);
            return ApiResult.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ApiResult.error(e.getMessage());
        }
    }

}
