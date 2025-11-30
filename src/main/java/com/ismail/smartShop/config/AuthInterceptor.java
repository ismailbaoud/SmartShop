package com.ismail.smartShop.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ismail.smartShop.annotation.RequireAdmin;
import com.ismail.smartShop.annotation.RequireAuth;
import com.ismail.smartShop.annotation.RequireClient;
import com.ismail.smartShop.model.enums.*;
import com.ismail.smartShop.model.User;

@Component
public class AuthInterceptor implements HandlerInterceptor {

@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (!(handler instanceof HandlerMethod)) {
        return true; // allow static resources
    }

    HandlerMethod hm = (HandlerMethod) handler;
    HttpSession session = request.getSession(false);

    Object userObj = session != null ? session.getAttribute("USER") : null;

    // Check @RequireAuth
    if (hm.getMethod().isAnnotationPresent(RequireAuth.class)) {
        if (userObj == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return false;
        }
    }

    // Check @RequireUser
    if (hm.getMethod().isAnnotationPresent(RequireClient.class)) {
        if (userObj == null || !Role.CLIENT.equals(((User) userObj).getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: User role required");
            return false;
        }
    }

    // Check @RequireAdmin
    if (hm.getMethod().isAnnotationPresent(RequireAdmin.class)) {
        if (userObj == null || !Role.ADMIN.equals(((User) userObj).getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: Admin role required");
            return false;
        }
    }

    return true; // all checks passed
}

}
