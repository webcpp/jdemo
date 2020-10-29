# jdemo
hi-nginx-java example 

# config

```nginx

hi_java_classpath "-Djava.class.path=.:/usr/local/nginx/java:/usr/local/nginx/java/hi-nginx-java.jar:/usr/local/nginx/java/jdemo.jar"

location ~ \.java {
            rewrite ^/(.*)\.java$ /$1 break;
            hi_need_kvdb on;
		    hi_kvdb_size 50;
		    hi_kvdb_expires 5m;
		    hi_java_servlet hi/jdemo;	
        }


```