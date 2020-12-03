# jdemo
hi-nginx-java example 

# config

```nginx

hi_java_classpath "-Djava.class.path=.:/usr/local/nginx/java:/usr/local/nginx/java/hi-nginx-java.jar:/usr/local/nginx/java/jdemo.jar"
hi_java_options "-server -d64  -Dnashorn.args=--global-per-engine";
hi_java_servlet_cache_expires 1h;
hi_java_servlet_cache_size 10;
hi_java_version 11;

location ~ \.java {
            rewrite ^/(.*)\.java$ /$1 break;
            hi_need_kvdb on;
            hi_kvdb_size 50;
            hi_kvdb_expires 5m;
            hi_need_session on;
            hi_need_headers on;
            hi_need_cookies on;
            hi_java_servlet hi/jdemo;
            #hi_java_servlet hi/gdemo;
        }


```