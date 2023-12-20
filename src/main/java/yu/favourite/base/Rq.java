package yu.favourite.base;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Map;

@Component
@RequestScope
public class Rq {
    private final HttpServletRequest req;

    public Rq(HttpServletRequest req) {

        this.req = req;

    }

    public String getParamsJsonStr() {
        Map<String, String[]> parameterMap = req.getParameterMap();
        return Ut.json.toStr(parameterMap);
    }
}
