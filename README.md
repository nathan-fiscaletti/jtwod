# JTwoD
> A Java game Engine

[![GitHub issues](https://img.shields.io/github/issues/nathan-fiscaletti/jtwod.svg)](https://github.com/nathan-fiscaletti/jtwod/issues) [![GitHub forks](https://img.shields.io/github/forks/nathan-fiscaletti/jtwod.svg)](https://github.com/nathan-fiscaletti/jtwod/network) [![GitHub stars](https://img.shields.io/github/stars/nathan-fiscaletti/jtwod.svg)](https://github.com/nathan-fiscaletti/jtwod/stargazers) [![GitHub license](https://img.shields.io/github/license/nathan-fiscaletti/jtwod.svg)](https://github.com/nathan-fiscaletti/jtwod/blob/master/LICENSE)

### Building

To build the library, clone the repository and use the provided build script.

* Use the `--debug` flag to compile the library with the source in tact. This will give you access to the Java doc comments for the library.
* Use the `--release` flag to compile the library without this documentation, at a much smaller size.

#### Unix

```sh
$ ./build.sh (--release | --debug)
```

#### Windows

```sh
C:\> build.bat (--release | --debug)
```

### Games currently using this engine

[Glide: A space shooter](http://github.com/nathan-fiscaletti/glide)

### Examples

See [src/jtwod/examples](https://github.com/nathan-fiscaletti/jtwod/tree/master/src/jtwod/examples) for some example implementations of this library.
