package hi

import hi.servlet
import hi.route
import static hi.route.get_instance as route_instance

import java.util.regex.Matcher
import java.util.ArrayList
import java.util.HashMap

class gdemo implements hi.servlet {

    static hi.route r = route_instance()

    public gdemo() {
        gdemo.r.get('^/(hello|test)/?$', (hi.request req, hi.response res, Matcher m) -> {
            this.do_hello(req, res)
        });
        gdemo.r.get('^/error/?$', (hi.request req, hi.response res, Matcher m) -> {
            this.do_error(req, res)
        });
        gdemo.r.get('^/redirect/?$', (hi.request req, hi.response res, Matcher m) -> {
            this.do_redirect(req, res)
        });
        gdemo.r.add(new ArrayList<String>(Arrays.asList('GET', 'POST')), '^/form/?$',
                (hi.request req, hi.response res, Matcher m) -> {
                    this.do_form(req, res)
                });
        gdemo.r.get('^/session/?$', (hi.request req, hi.response res, Matcher m) -> {
            this.do_session(req, res)
        });
        gdemo.r.get('^/md5/?$', (hi.request req, hi.response res, Matcher m) -> {
            this.do_md5(req, res)
        });
    }

    public void handler(hi.request req, hi.response res) {
        gdemo.r.run(req, res)
    }

    private void do_hello(hi.request req, hi.response res) {
        res.set_content_type('text/plain;charset=UTF-8');
        res.set_cookie('test-k', 'test-v', 'max-age=3; Path=/;');
        res.status = 200
        res.content = 'hello,world'
    }

    private void do_error(hi.request req, hi.response res) {
        res.set_content_type('text/plain;charset=UTF-8');
        res.status = 404
        res.content = '404 Not found'
    }

    private void do_redirect(hi.request req, hi.response res) {
        res.status = 302
        res.set_header('Location', '/hello.java')
    }

    private void do_form(hi.request req, hi.response res) {
        res.set_content_type('text/plain;charset=UTF-8');
        res.set_cookie('test-k', 'test-v', 'max-age=3; Path=/;');
        res.status = 200
        StringBuilder buffer = new StringBuilder()

        buffer.append('head data ' + req.headers.size() + '\n')
        buffer.append(this.do_foreach(req.headers))

        buffer.append('\ncookie data ' + req.cookies.size() + '\n')
        buffer.append(this.do_foreach(req.cookies))

        buffer.append('\nform data ' + req.form.size() + '\n')
        buffer.append(this.do_foreach(req.form))

        buffer.append(String.format('\nclient= %s\nmethod= %s\nuser_agent= %s\nuri= %s\nparam= %s\n', req.client,
                req.method, req.user_agent, req.uri, req.param))

        res.content = buffer.toString()
    }

    private void do_session(hi.request req, hi.response res) {
        res.set_content_type('text/plain;charset=UTF-8');
        res.set_cookie('test-k', 'test-v', 'max-age=3; Path=/;');
        res.status = 200
        String key = 'test'
        int value = 0
        if (req.session.containsKey(key)) {
            value = Integer.parseInt(req.session.get(key)) + 1
        }
        res.session.put(key, String.valueOf(value))
        res.content = String.format('hello,%d', value)
        res.status = 200
    }

    private void do_md5(hi.request req, hi.response res) {
        res.set_content_type('text/plain;charset=UTF-8');
        res.set_cookie('test-k', 'test-v', 'max-age=3; Path=/;');
        String plaintext = 'hello,md5!'
        res.status = 200
        res.content = String.format('%s\nmd5= %s', plaintext, plaintext.md5() )
    }

    private String do_foreach(HashMap<String, String> m) {
        StringBuffer buffer = new StringBuffer()
        m.each((k, v)->{
            buffer.append(String.format("%s\t=\t%s\n", k, v))
        })
        // or
        // for (item in m.entrySet()) {
        //     buffer.append(String.format("%s\t=\t%s\n", item.getKey(), item.getValue()))
        // }
        return buffer.toString()
    }

}
