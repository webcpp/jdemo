
SRC:=$(shell find . -name *.java -type f)
OBJ:=$(patsubst %.java,%.class,$(SRC))

GSRC:=$(shell find . -name *.groovy -type f)
GOBJ:=$(patsubst %.groovy,%.class,$(GSRC))

PRO=jdemo.jar

ifndef NGINX_INSTALL_DIR
NGINX_INSTALL_DIR=/usr/local/nginx
endif

JFLAGS =-classpath .:${CLASSPATH}
JC = ${JAVA_HOME}/bin/javac

GC = ${GROOVY_HOME}/bin/groovyc
GFLAGS = $(JFLAGS)

JAR = ${JAVA_HOME}/bin/jar

%.class:%.java
	$(JC) $(JFLAGS) $^

%.class:%.groovy
	$(GC) $(GFLAGS) $^

default: ${PRO}


${PRO}:$(OBJ) $(GOBJ)
	${JAR} --create --file ${PRO} ${OBJ} ${GOBJ}

clean:
	rm -f ${OBJ} $(GOBJ) ${PRO}

install:${OBJ}
	install ${PRO} $(NGINX_INSTALL_DIR)/java



