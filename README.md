# The Aprikot Library

Aprikot is yet another general-purpose Scala Library.

# How to use the Aprikot Library

In SBT settings, add my Maven repository to the resolvers.

    resolvers += "sumito3478 Maven Repository" at "http://maven.sumito3478.info/"

Then add aprikot to the library dependencies.

    libraryDependencies += "info.sumito3478" %% "aprikot" % "0.0.+"

Aprikot 0.1.x will be released soon.
After that, binary compatibility will be maintained in patch releases.

# How to build

Clone the repository and submodules (use `git clone --recursive`!) and
execute `./sbt`.
In SBT prompt, execute `compile`, `test`, etc.

    ╭─/var/sumito3478/misc/repo
    ╰─$ git clone --recursive https://github.com/sumito3478/aprikot.git
    ...
    ╭─/var/sumito3478/misc/repo
    ╰─$ cd aprikot
    ╭─/var/sumito3478/misc/repo/aprikot
    ╰─$ ./sbt
    ...
    > compile
    ...
    [success] Total time: 25 s, completed Feb 2, 2013 10:14:55 PM
    > test
    ...
    [success] Total time: 18 s, completed Feb 2, 2013 10:15:14 PM
    >

# Documentation

This software is in early development and not well documented, but has some
documentation comments in the source and with `sbt doc`, you can generate
API documents.

# Features

The root package of Aprikot is `info.sumito3478.aprikot`.

## aprikot.check

Contains code to calculate checksums.
Currently it supports 32bit/64bit CRC with arbitrary polynominals.
CRC32-C, CRC32-IEEE, CRC32-K, CRC64ISO and CRC-ISO specializations are
also available.

Example Usage:
```scala
import info.sumito3478.aprikot.check.CRC32

object crc32 extends CRC32 {
  def poly: Int = 0x04c11db7
}

val testData =
  "Mis, Mis, Mister, Drill, Driller, I'll do my best, I cant lose!".
    getBytes("UTF-8")

val crc32OfTestData = crc32(testData)

// crc32OfTestData == 0x2c78b2f6
```

## aprikot.collection

Some extensions to the Scala Collection Library.

## aprikot.combinator

A combinator library. It contains useful combinators such as forward pipeline
operator.

## aprikot.control

A control-structure library.

### breakable

Similar to `scala.util.control.Breaks.breakable`, but the break function is
passed as parameter.
It also supports returning value with break.

Example Usage:
```scala
import info.sutmio3478.aprikot.control.{breakable,__}
val j = true
val i = breakable[Int] {
  break =>
    if(j)
      break 0
    1
}
// i == 0
```

### callCC

This is a utility function for delimited continuation.
Calls the specified function with current continuation.

### generator

A generator implementation with delimited continuation.

## aprikot.hashing

A hash code library. Currently supports `CityHash v1.1`.

## aprikot.http

An HTTP library. Parsers and servers.

## aprikot.io

An asynchronous I/O library. Based on NIO2.

## aprikot.numeric

Contains numeric types.

## aprikot.parsing

Contains utilities for `scala.util.parsing`.

## aprikot.threading

Contains utilities related to threading.

## aprikot.time

Contains types related to time.

## aprikot.unmanaged

An unmanaged memory library.
It uses sun.misc.Unsafe for performance.

# License

Current version of the Aprikot Library is licensed under Affero GPL v3. See
COPYING.md for more details.
