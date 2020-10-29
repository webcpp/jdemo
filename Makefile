
SRC:=$(shell find . -name *.java -type f)
OBJ:=$(patsubst %.java,%.class,$(SRC))

PRO=jdemo.jar

ifndef NGINX_INSTALL_DIR
NGINX_INSTALL_DIR=/usr/local/nginx
endif

JFLAGS =-classpath .:${CLASSPATH}
JC = ${JAVA_HOME}/bin/javac
JAR = ${JAVA_HOME}/bin/jar

%.class:%.java
	$(JC) $(JFLAGS) $^


default: ${PRO}


${PRO}:$(OBJ)
		${JAR} --create --file ${PRO} ${OBJ}

clean:
	rm -f ${OBJ} ${PRO}

install:${OBJ}
	install ${PRO} $(NGINX_INSTALL_DIR)/java



