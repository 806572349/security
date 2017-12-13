package com.imooc.security.brower;

import com.imooc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowerController {
    private RequestCache cache=new HttpSessionRequestCache();
     //日志
      private Logger logger= LoggerFactory.getLogger(getClass());
    private RedirectStrategy redirectStrategy=new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @GetMapping("/authenticaiton/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ModelMap require(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest cacheRequest = cache.getRequest(request, response);
        if (cacheRequest!=null){
            String redirectUrl = cacheRequest.getRedirectUrl();
            logger.info("跳转的连接是"+redirectUrl);
            if (StringUtils.endsWithIgnoreCase(redirectUrl,".html")){
                redirectStrategy.sendRedirect(request,response,securityProperties.getBrower().getLoginPage());

            }

        }
        logger.info("需要认证");
        ModelMap modelMap = new ModelMap();
        modelMap.put("code","1");
        modelMap.put("msg","需要认证");
        return  modelMap;
    }


}
