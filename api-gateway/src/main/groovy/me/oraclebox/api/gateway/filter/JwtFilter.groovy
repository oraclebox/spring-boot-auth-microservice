package me.oraclebox.api.gateway.filter

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import me.oraclebox.api.gateway.property.ApplicationProperty
import me.oraclebox.http.ResultModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate

import javax.servlet.http.HttpServletRequest

/**
 * Filter use to validate JWT token in request header if path is start from /private/
 * 篩選器用於對每一個開始為/private的HTTP請求，驗證標頭中的JWT
 * Created by oraclebox on 11/24/2016.
 */
class JwtFilter extends ZuulFilter {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ApplicationProperty property;

    private static Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    String filterType() {
        return "pre";
    }

    @Override
    int filterOrder() {
        return 0;
    }

    @Override
    boolean shouldFilter() {
        return true;
    }

    @Override
    Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        // Require check JWT header
        if (request.getRequestURL().toString().indexOf("/private/") > 0) {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing Authorization header and token.");
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(403);
                return null;
            }

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", authHeader);
                HttpEntity entity = new HttpEntity(headers);

                HttpEntity<ResultModel> response = restTemplate.exchange(
                        property.authServiceUrl, HttpMethod.GET, entity, ResultModel.class);
            }catch(Exception e){
                e.printStackTrace();
                log.error("Cannot validate token: "+authHeader+" from "+property.authServiceUrl);
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(403);
            }
            log.debug(property.authServiceUrl);
        }
        return null;
    }
}
