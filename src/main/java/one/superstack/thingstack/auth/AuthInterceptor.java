package one.superstack.thingstack.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import one.superstack.thingstack.auth.actor.AuthenticatedActor;
import one.superstack.thingstack.auth.actor.Jwt;
import one.superstack.thingstack.auth.actor.RequiresAuthentication;
import one.superstack.thingstack.auth.actor.ThreadLocalWrapper;
import one.superstack.thingstack.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final Jwt jwt;

    private static final String BEARER_PREFIX = "Bearer";
    private static final String API_KEY_PREFIX = "ApiKey";
    private static final String APP_KEY_PREFIX = "AppKey";

    @Autowired
    public AuthInterceptor(Jwt jwt) {
        this.jwt = jwt;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Object handlerBean = ((HandlerMethod) handler).getBean();

if (handlerBean instanceof RequiresAuthentication) {
                AuthorizationHeader header = extractAuthorizationData(request);
                AuthenticatedActor actor = getActor(header);
                ThreadLocalWrapper.setActor(actor);
            }
        }

        return true;
    }

    private AuthenticatedActor getActor(AuthorizationHeader header) {
        AuthenticatedActor actor;

        if (header.getType().equals(BEARER_PREFIX)) {
            String jwtToken = header.getContent();
            actor = jwt.getActor(jwtToken);
            if (null == actor) {
                throw new InvalidTokenException();
            }
        } else {
            throw new InvalidTokenException();
        }

        return actor;
    }

    private AuthorizationHeader extractAuthorizationData(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            throw new InvalidTokenException();
        }

        String[] authorizationComponents = authorizationHeader.split("\\s+");

        if (authorizationComponents.length != 2) {
            throw new InvalidTokenException();
        }

        String tokenType = authorizationComponents[0];
        if (!tokenType.equals(BEARER_PREFIX) && !tokenType.equals(API_KEY_PREFIX) && !tokenType.equals(APP_KEY_PREFIX)) {
            throw new InvalidTokenException();
        }

        return new AuthorizationHeader(tokenType, authorizationComponents[1]);
    }
}
