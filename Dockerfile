FROM goodforgod/debian-jdk10-oracle:sid

# Install curl & unzip
RUN apt-get update && \
    apt-get -y install curl && \
    apt-get -y install unzip

# Install gradle
RUN mkdir /opt/gradle && \
    cd /opt/gradle && \
    curl -L https://services.gradle.org/distributions/gradle-4.10.2-bin.zip -o gradle-4.10.2-bin.zip && \
    unzip -q gradle-4.10.2-bin.zip && \
    rm gradle-4.10.2-bin.zip

# Export some environment variables
ENV GRADLE_HOME=/opt/gradle/gradle-4.10.2
ENV PATH=$PATH:$GRADLE_HOME/bin

# Download project, build it, remove installed stuff
RUN cd / && \
    curl -LOk https://github.com/GoodforGod/money-manager/archive/master.zip && \
    unzip -q master.zip && \
    cd money-manager* && \
    gradle build && \
    apt-get -y remove curl && \
    apt-get -y remove unzip && \
    apt-get -y autoremove

ENTRYPOINT ["java", "-jar", "/money-manager-master/build/libs/money-manager-all-1.0.0.jar"]