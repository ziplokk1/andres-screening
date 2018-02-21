FROM debian:jessie

RUN apt-get update && apt-get install -y \
    git \
    curl \
    jq \
    default-jre \
    default-jdk

WORKDIR /root
RUN ["/bin/bash", "-c", "mkdir data && cd data && while read i; do git clone $i; done < <(curl -s https://api.github.com/orgs/datasets/repos?per_page=100 | jq -r '.[].clone_url')"]

RUN mkdir code && mkdir bin

COPY src code/

RUN javac /root/code/com/iheart/*.java -d /root/bin

WORKDIR /root/bin

ENTRYPOINT ["java"]
CMD ["com.iheart.Main", "/root/data"]