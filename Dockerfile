FROM openjdk
RUN mkdir -p /usr/src/myapp
COPY CoinMarketCap.java /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac CoinMarketCap.java
ENTRYPOINT ["java", "CoinMarketCap"]
