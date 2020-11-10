package hi

import hi.servlet

import java.util.ArrayList
import java.util.HashMap

class test implements hi.servlet {

    public void handler(hi.request req, hi.response res) {
        if (req.method.equals('GET')) {
            if (req.uri.matches('^/(hello|test)/?$')) {
                this.do_hello(req, res)
            } else if (req.uri.matches('^/error/?$')) {
                this.do_error(req, res)
            } else if (req.uri.matches('^/redirect/?$')) {
                this.do_redirect(req, res)
            } else if (req.uri.matches('^/form/?$')) {
                this.do_form(req, res)
            } else if (req.uri.matches('^/session/?$')) {
                this.do_session(req, res)
            } else if (req.uri.matches('^/md5/?$')) {
                this.do_md5(req, res)
            } else {
                this.do_error(req, res)
            }
        }
    }

    private void do_hello(hi.request req, hi.response res) {
        res.headers.get('Content-Type').set(0, 'text/plain;charset=UTF-8');
        res.status = 200
        res.content = 'hello,world'
    }

    private void do_error(hi.request req, hi.response res) {
        res.headers.get('Content-Type').set(0, 'text/plain;charset=UTF-8');
        res.status = 404
        res.content = '404 Not found'
    }

    private void do_redirect(hi.request req, hi.response res) {
        res.status = 302
        ArrayList<String> h = new ArrayList<String>()
        h.add('/hello.java')
        res.headers.put('Location', h)
    }

    private void do_form(hi.request req, hi.response res) {
        res.headers.get('Content-Type').set(0, 'text/plain;charset=UTF-8');
        res.status = 200

        res.content = 'head data ' + req.headers.size() + '\n'
        res.content = res.content.concat(this.do_foreach(req.headers))

        res.content = res.content.concat('\ncookie data ') + req.cookies.size() + '\n'
        ;
        res.content = res.content.concat(this.do_foreach(req.cookies))

        res.content = res.content.concat('\nform data ') + req.form.size() + '\n'
        ;
        res.content = res.content.concat(this.do_foreach(req.form))

        res.content = res.content.concat(
                String.format('\n%s\n%s\n%s\n%s\n%s\n', req.client, req.method, req.user_agent, req.uri, req.param))
    }

    private void do_session(hi.request req, hi.response res) {
        res.headers.get('Content-Type').set(0, 'text/plain;charset=UTF-8');
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
        res.headers.get('Content-Type').set(0, 'text/plain;charset=UTF-8');
        String plaintext = 'hello,md5!'
        res.status = 200
        res.content = String.format('%s\nmd5= %s', plaintext, plaintext.md5() )
    }

    private String do_foreach(HashMap<String, String> m) {
        String result = ''
        for (String k : m.keySet()) {
            result = result.concat(String.format("%s\t=\t%s\n", k, m.get(k)))
        }
        return result
    }

}
