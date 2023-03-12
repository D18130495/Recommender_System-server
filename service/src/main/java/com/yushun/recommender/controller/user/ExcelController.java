package com.yushun.recommender.controller.user;

import com.yushun.recommender.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

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
    public void exportUserData(HttpServletResponse response,
                               @PathParam("email") String email) {
        excelService.exportUserData(response, email);
    }
}
