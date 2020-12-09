package test;

import hi.request;
import hi.response;
import hi.route;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Arrays;
import java.io.File;
import java.io.FileReader;
import com.samskivert.mustache.Template;

public class filetemplate implements hi.route.run_t {

    public filetemplate() {
    }

    public void handler(hi.request req, hi.response res, Matcher m) {
        res.set_content_type("text/plain;charset=UTF-8");
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("title", "文件模板渲染测试");
        Object persons = Arrays.asList(new Object() {
            String name = "张三";
            int age = 70;
        }, new Object() {
            String name = "李四";
            int age = 59;
        });
        data.put("persons", persons);
        try {
            Template tmpl = hi.route.get_instance().get_compiler().compile(new FileReader(
                    hi.route.get_instance().get_config().getString("template.directory") + "/main.mustache"));
            res.content = tmpl.execute(data);
            res.status = 200;
        } catch (Exception e) {
            res.content = e.getMessage();
            res.status = 500;
        }
    }

}