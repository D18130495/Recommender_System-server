package com.yushun.recommender.controller.user;

import com.yushun.recommender.service.ExcelService;
import com.yushun.recommender.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Excel Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-7
 */
@CrossOrigin
@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private ExcelService excelService;

    @GetMapping("/exportUserData")
    public void exportUserData(HttpServletResponse response) {
        excelService.exportUserData(response);
    }
}
