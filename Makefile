
PRO=jdemo.jar

ifndef NGINX_INSTALL_DIR
NGINX_INSTALL_DIR=/usr/local/nginx
endif


JAVAC = ${JAVA_HOME}/bin/javac
JAVA_FLAGS =-classpath .:${CLASSPATH}

GROOVYC = ${GROOVY_HOME}/bin/groovyc
GROOVY_FLAGS = $(JFLAGS)

JAR = ${JAVA_HOME}/bin/jar
JAR_FLAGS = cfv



default: ${PRO}

${PRO}:
	find . -name *.java -type f > jsrc.list && \
	$(JAVAC) $(JAVA_FLAGS) @jsrc.list
	find . -name *.groovy -type f > gsrc.list && \
	$(GROOVYC) $(GROOVY_FLAGS) @gsrc.list 
	find . -name *.class -type f > class.list && \
	$(JAR) $(JAR_FLAGS) ${PRO} `cat class.list`


clean:
	rm -f ${PRO} jsrc.list gsrc.list `cat class.list` class.list


jmeter:
	if [ -d jmeter_test ];then rm -rf jmeter_test;fi
	mkdir jmeter_test
	/usr/lib/jvm/apache-jmeter-5.3/bin/jmeter -n -t test.jmx -l jmeter_test/ret -e -o jmeter_test

install:${OBJ}
	install ${PRO} $(NGINX_INSTALL_DIR)/java



