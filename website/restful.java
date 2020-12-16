package website;

import hi.servlet;
import hi.request;
import hi.response;
import hi.route;

import java.util.regex.Matcher;

import website.restful_init;

public class restful implements hi.servlet {

    public restful() {
        restful_init.init();
    }

    public void handler(hi.request req, hi.response res) {
        hi.route.get_instance().run(req, res);
    }

}