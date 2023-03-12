package com.yushun.recommender.service;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Excel Service
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-7
 */
public interface ExcelService {
    void exportUserData(HttpServletResponse response, String email);
}
