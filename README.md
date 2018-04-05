# DockerDemo

This project is a demonstration of running some Java code inside a docker image. The application calls out to the CoinMarketCap API and returns price and market cap information for the currency specified on the command line.

NOTE: if you get permission denied errors when running the commands listed below, you will may need to prefix all the commands with sudo, or add your user to the docker group as explained here: https://askubuntu.com/a/739861/76288

## Building the new docker image

Build the new docker image by running the following command:

```console
docker build -t coinmarketcap .
```


## Running the new docker image

You can then run the image as follows:

```console
docker run --rm --name "CoinMarketCap" coinmarketcap CURRENCYID
```

where CURRENCYID is the currency you want to use, such as bitcoin, ethereum, etc.
There are two extra functions I added. You can format the output in JSON by adding json at the end of the command:

```console
docker run --rm --name "CoinMarketCap" coinmarketcap CURRENCYID json
```

Also, you can list all the currencies available with the argument list:

```console
docker run --rm --name "CoinMarketCap" coinmarketcap list
```

if you do not pass any arguments at the end, it will display usage text.


## Running without creating a new image

There is also a way to run it inside a container without creating a new docker image. If you compile the java class such that you have CoinMarketCap.class and CoinMarketCap$AppException.class in the current directory. You can run the following command to run these files inside an openjdk container:

```console
docker run --rm --name "CoinMarketCap" -v /`pwd`://app -w //app openjdk java CoinMarketCap list
```

The openjdk image uses java 8, so make sure you compile -release 8 if you're using jdk 9 or 10.