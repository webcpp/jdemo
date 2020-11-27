
package hi;

import hi.servlet;
import hi.jdemo_init;

public class jdemo implements hi.servlet {

    public jdemo() {
    }

    public void handler(hi.request req, hi.response res) {
        jdemo_init.get_instance().get_route().run(req, res);
    }

}
