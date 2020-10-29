
package hi;

public class jdemo implements hi.servlet {

    public jdemo() {

    }

    public void handler(hi.request req, hi.response res) {
        res.headers.get("Content-Type").set(0, new String("text/plain;charset=UTF-8"));
        res.status = 200;
        res.content = "Hello World";
    }
}
