
package hi;

import hi.servlet;
import hi.request;
import hi.response;
import hi.route;
import hi.jdemo_init;

public class jdemo implements hi.servlet {

    public jdemo() {
        hi.jdemo_init.get_instance();
    }

    public void handler(hi.request req, hi.response res) {
        hi.route.get_instance().run(req, res);
    }

}
