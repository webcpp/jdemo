
PRO=jdemo.jar

ifndef NGINX_INSTALL_DIR
NGINX_INSTALL_DIR=/usr/local/nginx
endif


JAVAC = ${JAVA_HOME}/bin/javac
JAVA_FLAGS =-classpath .:${CLASSPATH}

GROOVYC = ${GROOVY_HOME}/bin/groovyc
GROOVY_FLAGS = $(JAVA_FLAGS)


SCALAC = ${SCALA_HOME}/bin/scalac
SCALAC_FLAGS = $(JAVA_FLAGS)

KOTLINC = ${KOTLIN_HOME}/bin/kotlinc
KOTLINC_FLAGS = $(JAVA_FLAGS) -jvm-target 1.8

JAR = ${JAVA_HOME}/bin/jar
JAR_FLAGS = cfv



default: ${PRO}

${PRO}:
	find . -name *.java -type f > java_src.list && \
	$(JAVAC) $(JAVA_FLAGS) @java_src.list
	find . -name *.groovy -type f > groovy_src.list && \
	$(GROOVYC) $(GROOVY_FLAGS) @groovy_src.list 
	find . -name *.scala -type f > scala_src.list && \
	$(SCALAC) $(SCALAC_FLAGS) @scala_src.list
	find . -name *.kt -type f > kotlin_src.list && \
	$(KOTLINC) $(KOTLINC_FLAGS) @kotlin_src.list
	find . -name *.class -type f > class.list && \
	$(JAR) $(JAR_FLAGS) ${PRO} `cat class.list`


clean:
	rm -f ${PRO} java_src.list groovy_src.list scala_src.list kotlin_src.list `cat class.list` class.list


jmeter:
	if [ -d benchmark ];then rm -rf benchmark;fi
	mkdir benchmark
	/usr/lib/jvm/apache-jmeter-5.3/bin/jmeter -n -t benchmark.jmx -l benchmark/ret -e -o benchmark

install:${OBJ}
	install ${PRO} $(NGINX_INSTALL_DIR)/java



