package hi

import hi.servlet
import hi.route
import static hi.route.get_instance as route_instance

import java.util.regex.Matcher
import java.util.ArrayList
import java.util.HashMap
import com.samskivert.mustache.Template
import com.samskivert.mustache.Mustache
import com.google.gson.Gson

class gdemo implements hi.servlet {

    static hi.route r = route_instance()

    public gdemo() {
        gdemo.r.get('^/(hello|test)/?$', this::do_hello)
        gdemo.r.get('^/error/?$', this::do_error);
        gdemo.r.get('^/redirect/?$', this::do_redirect)
        gdemo.r.add(new ArrayList<String>(Arrays.asList('GET', 'POST')), '^/form/?$',
               this::do_form)
        gdemo.r.get('^/session/?$', this::do_session)
        gdemo.r.get('^/md5/?$', this::do_md5)
        gdemo.r.get('^/template/?$', this::do_template)
        gdemo.r.get('^/gson/?$', this::do_gson)
        gdemo.r.add(new ArrayList<String>(Arrays.asList('GET')), '^/helloworld/?', 'hi.helloworld')
    }

    public void handler(hi.request req, hi.response res) {
        gdemo.r.run(req, res)
    }

    private void do_hello(hi.request req, hi.response res, Matcher m) {
        res.set_content_type('text/plain;charset=UTF-8');
        res.set_cookie('test-k', 'test-v', 'max-age=3; Path=/;');
        res.status = 200
        res.content = 'hello,world'
    }

    private void do_error(hi.request req, hi.response res, Matcher m) {
        res.set_content_type('text/plain;charset=UTF-8');
        res.status = 404
        res.content = '404 Not found'
    }

    private void do_redirect(hi.request req, hi.response res, Matcher m) {
        res.status = 302
        res.set_header('Location', '/hello.java')
    }

    private void do_form(hi.request req, hi.response res, Matcher m) {
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

    private void do_session(hi.request req, hi.response res, Matcher m) {
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

    private void do_md5(hi.request req, hi.response res, Matcher m) {
        res.set_content_type('text/plain;charset=UTF-8');
        res.set_cookie('test-k', 'test-v', 'max-age=3; Path=/;');
        String plaintext = 'hello,md5!'
        res.status = 200
        res.content = String.format('%s\nmd5= %s', plaintext, plaintext.md5() )
    }

    private void do_template(hi.request req, hi.response res, Matcher m) {
        String text = 'One, two, {{three}}. Three sir!'
        Template tmpl = Mustache.compiler().compile(text)
        Map<String, String> data = new HashMap<String, String>()
        data.put('three', 'five')
        try {
            res.content = tmpl.execute(data)
            res.status = 200
        } catch (Exception e) {
            res.status = 500
            res.content = 'mustache exception.'
        }
    }

    private void do_gson(hi.request req, hi.response res, Matcher m) {
        Gson gson = new Gson()
        HashMap<String, Object> map = new HashMap<String, Object>()
        map.put('name', 'JTZen9')
        map.put('age', 21)
        map.put('sex', 'male')
        res.content = gson.toJson(map)
        res.status = 200
        res.set_content_type('Content-type: application/json')
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
