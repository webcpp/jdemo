package mykotlin

import hi.request
import hi.response
import hi.route
import java.util.regex.Matcher


class test:hi.route.run_t {
    override fun handler(req: hi.request, res: hi.response, m: Matcher){
        res.content = "hello,kotlin\n"
        res.status = 200
        res.set_content_type("text/plain;charset=utf-8")
    }
}