
package hi;

import hi.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class jdemo implements hi.servlet {

    public jdemo() {

    }

    public void handler(hi.request req, hi.response res) {

        if (req.method.equals("GET")) {
            if (Pattern.matches("^/hello/?$", req.uri)) {
                this.do_hello(req, res);
            } else if (Pattern.matches("^/error/?$", req.uri)) {
                this.do_error(req, res);
            } else if (Pattern.matches("^/redirect/?$", req.uri)) {
                this.do_redirect(req, res);
            } else if (Pattern.matches("^/form/?$", req.uri)) {
                this.do_form(req, res);
            } else if (Pattern.matches("^/session/?$", req.uri)) {
                this.do_session(req, res);
            } else if (Pattern.matches("^/md5/?$", req.uri)) {
                this.do_md5(req, res);
            } else {
                this.do_error(req, res);
            }

        }
    }

    private void do_hello(hi.request req, hi.response res) {
        res.headers.get("Content-Type").set(0, "text/plain;charset=UTF-8");
        res.status = 200;
        res.content = "hello,world";
    }

    private void do_error(hi.request req, hi.response res) {
        res.headers.get("Content-Type").set(0, "text/plain;charset=UTF-8");
        res.status = 404;
        res.content = "404 Not found";
    }

    private void do_redirect(hi.request req, hi.response res) {
        res.status = 302;
        ArrayList<String> h = new ArrayList<String>();
        h.add("/hello.java");
        res.headers.put("Location", h);
    }

    private void do_form(hi.request req, hi.response res) {
        res.headers.get("Content-Type").set(0, "text/plain;charset=UTF-8");
        res.status = 200;

        res.content = "head data " + req.headers.size() + "\n";
        res.content = res.content.concat(this.do_foreach(req.headers));

        res.content = res.content.concat("\ncookie data ") + req.cookies.size() + "\n";
        ;
        res.content = res.content.concat(this.do_foreach(req.cookies));

        res.content = res.content.concat("\nform data ") + req.form.size() + "\n";
        ;
        res.content = res.content.concat(this.do_foreach(req.form));

        res.content = res.content.concat(
                String.format("\n%s\n%s\n%s\n%s\n%s\n", req.client, req.method, req.user_agent, req.uri, req.param));
    }

    private void do_session(hi.request req, hi.response res) {
        res.headers.get("Content-Type").set(0, "text/plain;charset=UTF-8");
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

    private void do_md5(hi.request req, hi.response res) {
        res.headers.get("Content-Type").set(0, "text/plain;charset=UTF-8");
        String plaintext = "hello,md5!";
        res.status = 200;
        res.content = String.format("%s\nmd5= %s", plaintext, this.md5(plaintext));
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
        String result = "";
        for (String k : m.keySet()) {
            result = result.concat(String.format("%s\t=\t%s\n", k, m.get(k)));
        }
        return result;
    }
}
