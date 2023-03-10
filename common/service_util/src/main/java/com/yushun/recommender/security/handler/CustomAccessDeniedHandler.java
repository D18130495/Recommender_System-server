package com.yushun.recommender.security.handler;

import com.google.gson.Gson;
import com.yushun.recommender.security.result.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * Custom Access Denied Handler
 * Token without permission
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-12
 */

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(new Gson().toJson(Result.permissionDenied()));
//        response.getWriter().write(new ObjectMapper().writeValueAsString(Result.permissionDenied()));
    }
}
