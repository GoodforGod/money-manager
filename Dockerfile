FROM java:8

# Install Curl
RUN apt -y install curl && \
    apt -y install unzip

RUN mkdir /opt/gradle && \
    cd /opt/gradle && \
    curl -L https://services.gradle.org/distributions/gradle-4.10.2-bin.zip -o gradle-4.10.2-bin.zip && \
    unzip -q gradle-4.10.2-bin.zip && \
    rm gradle-4.10.2-bin.zip

# Export some environment variables
ENV GRADLE_HOME=/opt/gradle/gradle-4.10.2
ENV PATH=$PATH:$GRADLE_HOME/bin

RUN cd / && \
    curl -LOk https://github.com/GoodforGod/money-manager/archive/master.zip && \
    unzip master.zip && \
    cd money-manager* && \
    gradle build && \
    apt -y remove curl && \
    apt -y remove unzip && \
    apt -y autoremove

ENTRYPOINT ["java", "-jar", "/money-manager-master/build/libs/money-manager-all-1.0.0.jar"]