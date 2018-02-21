FROM hseeberger/scala-sbt

RUN apt-get update && apt-get install -y \
    git \
    curl \
    jq

WORKDIR /root
RUN ["/bin/bash", "-c", "mkdir data && cd data && while read i; do git clone $i; done < <(curl -s https://api.github.com/orgs/datasets/repos?per_page=100 | jq -r '.[].clone_url')"]

RUN mkdir /root/code

COPY . /root/code/

WORKDIR /root/code/screening/src/main/scala

RUN ["/root/scala-2.12.2/bin/scalac", "main.scala"]

ENTRYPOINT ["/root/scala-2.12.2/bin/scala"]
CMD ["scala.Main", "/root/data"]
