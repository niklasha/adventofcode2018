# adventofcode2018
These are my, [Niklas Hallqvist](https://github.com/niklasha) solutions to
[Advent of code 2018](https://adventofcode.com/2018).
They are written in [Scala](https://scala-lang.org) using the
[ScalaTest](http://scalatest.org) testing framework.

My reason for doing these are, besides the fact that I like puzzle solving, I want to improve my Scala skills even more.

You need Scala, [sbt](https://scala-sbt.org), ScalaTest and of course
a [Java](https://java.com) runtime installed to run the tests, but that is about it.

Run all the days with:
```
sbt test
```

or a specific day with:
```
sbt "testOnly se.hallqvist.adventofcode2018.day1.*"
```

My results were:
```
      -------Part 1--------   -------Part 2--------
Day       Time  Rank  Score       Time  Rank  Score
 20   01:56:07   388      0   09:54:30  1158      0
 19   00:35:21   552      0   02:57:58   710      0
 18   00:35:33   687      0   00:53:42   562      0
 17   02:17:41   352      0   02:22:36   354      0
 16   01:22:10   818      0   01:50:47   686      0
 15   07:23:26   689      0       >24h  3689      0
 14   01:24:33  1626      0   02:43:57  1671      0
 13   05:25:11  2161      0   05:55:40  1818      0
 12   00:41:22   774      0   02:14:36  1314      0
 11   00:17:26   682      0   07:11:05  4113      0
 10   01:24:49  1540      0   01:27:37  1551      0
  9   00:50:14   953      0   02:10:34  1172      0
  8   00:52:28  1382      0   01:10:18  1298      0
  7   00:53:19  1440      0   02:47:39  1774      0
  6   04:37:31  3646      0   05:54:31  3744      0
  5   00:19:42  1103      0   00:32:42  1028      0
  4   03:36:16  3669      0   03:54:12  3552      0
  3   00:31:57  1551      0   01:00:33  1964      0
  2   00:19:50  1625      0   00:45:20  1822      0
  1   00:15:02  1427      0   00:47:25  1443      0
```
