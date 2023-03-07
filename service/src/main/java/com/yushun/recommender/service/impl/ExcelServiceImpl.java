package com.yushun.recommender.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.yushun.recommender.excel.UserDataExportVo;
import com.yushun.recommender.service.ExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Excel Service Implementation
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-7
 */

@Service
public class ExcelServiceImpl implements ExcelService {
    @Override
    public void exportUserData(HttpServletResponse response) {
        Workbook workBook = null;

        try {
            ExportParams userExportParams = new ExportParams();

            userExportParams.setSheetName("User detail");

            userExportParams.setTitle("User detail");

            Map<String, Object> userExportMap = new HashMap<>();

            userExportMap.put("title", userExportParams);

            userExportMap.put("entity", UserDataExportVo.class);

            UserDataExportVo userDataExportVo = new UserDataExportVo();
            userDataExportVo.setUsername("test");

            userExportMap.put("data", userDataExportVo);

            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(userExportMap);

            workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);

            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            response.setContentType("application/octet-stream");

            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户及操作日志导出.xls", StandardCharsets.UTF_8.name()));

            workBook.write(response.getOutputStream());
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(workBook != null) {
                try {
                    workBook.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
