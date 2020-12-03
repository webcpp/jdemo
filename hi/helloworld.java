package hi;

import hi.request;
import hi.response;
import hi.route;
import java.util.regex.Matcher;

public class helloworld implements hi.route.run_t {
    public helloworld() {
    }

    public void handler(request req, response res, Matcher m) {
        res.set_content_type("text/plain;charset=utf-8");
        res.content = "hello,world\n";
        res.status = 200;
    }
}
