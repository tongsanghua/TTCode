package com.tt.code.handler;

import com.tt.code.constants.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.Arrays;

@Component
@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String remoteAddr = request.getRemoteAddr();
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        if (Arrays.asList(UserConstants.LOCALHOST_ADDR).contains(remoteAddr) || remoteAddr.equals(hostAddress)) {
            AccountHandler.set(UserConstants.ADMIN_USER);
        } else {
            AccountHandler.set(remoteAddr);
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AccountHandler.clear();
    }

}
