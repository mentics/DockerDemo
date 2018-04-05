# DockerDemo

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


## Running without creating a new image

There is also a way to run it inside docker without creating a new docker image. If you compile the java class in the current directory, so you'll have CoinMarketCap.class and CoinMarketCap$AppException.class in the current directory. You can run the following command to run these files inside an openjdk container:

```console
docker run --rm --name "CoinMarketCap" -v /`pwd`://app -w //app openjdk java CoinMarketCap list
```