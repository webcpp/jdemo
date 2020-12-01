package hi;

import hi.servlet;
import hi.route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import com.samskivert.mustache.Template;
import com.samskivert.mustache.Mustache;
import com.google.gson.Gson;

class jdemo_init {
    private static hi.route r = hi.route.get_instance();
    private static jdemo_init instance = new jdemo_init();

    public static jdemo_init get_instance() {
        return jdemo_init.instance;
    }

    public hi.route get_route() {
        return jdemo_init.r;
    }

    private jdemo_init() {
        jdemo_init.r.get("^/(hello|test)/?$", (hi.request req, hi.response res, Matcher m) -> {
            this.do_hello(req, res, m);
        });
        jdemo_init.r.get("^/error/?$", (hi.request req, hi.response res, Matcher m) -> {
            this.do_error(req, res, m);
        });
        jdemo_init.r.get("^/redirect/?$", (hi.request req, hi.response res, Matcher m) -> {
            this.do_redirect(req, res, m);
        });
        jdemo_init.r.add(new ArrayList<String>(Arrays.asList("GET", "POST")), "^/form/?$",
                (hi.request req, hi.response res, Matcher m) -> {
                    this.do_form(req, res, m);
                });
        jdemo_init.r.get("^/session/?$", (hi.request req, hi.response res, Matcher m) -> {
            this.do_session(req, res, m);
        });
        jdemo_init.r.get("^/md5/?$", (hi.request req, hi.response res, Matcher m) -> {
            this.do_md5(req, res, m);
        });
        jdemo_init.r.get("^/template/?", (hi.request req, hi.response res, Matcher m) -> {
            this.do_template(req, res, m);
        });
        jdemo_init.r.get("^/gson/?", (hi.request req, hi.response res, Matcher m) -> {
            this.do_gson(req, res, m);
        });
    }

    private void do_hello(hi.request req, hi.response res, Matcher m) {
        res.set_content_type("text/plain;charset=UTF-8");
        res.set_cookie("test-k", "test-v", "Max-Age=3; Path=/");
        res.status = 200;
        res.content = "hello,world";
    }

    private void do_error(hi.request req, hi.response res, Matcher m) {
        res.set_content_type("text/plain;charset=UTF-8");
        res.status = 404;
        res.content = "404 Not found";
    }

    private void do_redirect(hi.request req, hi.response res, Matcher m) {
        res.status = 302;
        res.set_header("Location", "/hello.java");
    }

    private void do_form(hi.request req, hi.response res, Matcher m) {
        res.set_content_type("text/plain;charset=UTF-8");
        res.set_cookie("test-k", "test-v", "max-age=3; Path=/;");
        res.status = 200;
        StringBuilder buffer = new StringBuilder();

        buffer.append("head data " + req.headers.size() + "\n");
        buffer.append(this.do_foreach(req.headers));

        buffer.append("\ncookie data " + req.cookies.size() + "\n");
        buffer.append(this.do_foreach(req.cookies));

        buffer.append("\nform data " + req.form.size() + "\n");
        buffer.append(this.do_foreach(req.form));

        buffer.append(String.format("\nclient= %s\nmethod= %s\nuser_agent= %s\nuri= %s\nparam= %s\n", req.client,
                req.method, req.user_agent, req.uri, req.param));

        res.content = buffer.toString();
    }

    private void do_session(hi.request req, hi.response res, Matcher m) {
        res.set_content_type("text/plain;charset=UTF-8");
        res.set_cookie("test-k", "test-v", "max-age=3; Path=/;");
        res.status = 200;
        String key = "test";
        int value = 0;
        if (req.session.containsKey(key)) {
            value = Integer.parseInt(req.session.get(key)) + 1;
        }
        res.session.put(key, String.valueOf(value));
        res.content = String.format("hello,%d", value);
        res.status = 200;
    }

    private void do_md5(hi.request req, hi.response res, Matcher m) {
        res.set_content_type("text/plain;charset=UTF-8");
        res.set_cookie("test-k", "test-v", "max-age=3; Path=/;");
        String plaintext = "hello,md5!";
        res.status = 200;
        res.content = String.format("%s\nmd5= %s", plaintext, this.md5(plaintext));
    }

    private void do_template(hi.request req, hi.response res, Matcher m) {
        String text = "One, two, {{three}}. Three sir!";
        Template tmpl = Mustache.compiler().compile(text);
        Map<String, String> data = new HashMap<String, String>();
        data.put("three", "five");
        try {
            res.content = tmpl.execute(data);
            res.status = 200;
        } catch (Exception e) {
            res.status = 500;
            res.content = "mustache exception.";
        }
    }

    private void do_gson(hi.request req, hi.response res, Matcher m) {
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", "JTZen9");
        map.put("age", 21);
        map.put("sex", "male");
        res.content = gson.toJson(map);
        res.status = 200;
        res.set_content_type("Content-type: application/json");
    }

    private String md5(String str) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    private String do_foreach(HashMap<String, String> m) {
        StringBuffer buffer = new StringBuffer();
        for (HashMap.Entry<String, String> item : m.entrySet()) {
            buffer.append(String.format("%s\t=\t%s\n", item.getKey(), item.getValue()));
        }
        return buffer.toString();
    }
}