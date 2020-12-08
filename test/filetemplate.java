package test;

import hi.request;
import hi.response;
import hi.route;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Arrays;
import java.io.File;
import java.io.FileReader;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

public class filetemplate implements hi.route.run_t {

    public filetemplate() {
    }

    public void handler(hi.request req, hi.response res, Matcher m) {
        res.set_content_type("text/plain;charset=UTF-8");
        Mustache.TemplateLoader loader = (String name) -> {
            return new FileReader(new File("java/templates", name));
        };
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
            Template tmpl = Mustache.compiler().withLoader(loader)
                    .compile(new FileReader("java/templates/main.mustache"));
            res.content = tmpl.execute(data);
            res.status = 200;
        } catch (Exception e) {
            res.content = e.getMessage();
            res.status = 500;
        }
    }

}