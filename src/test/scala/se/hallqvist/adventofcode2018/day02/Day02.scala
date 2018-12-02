package se.hallqvist.adventofcode2018.day02

import org.scalatest._

/*
--- Day 2: Inventory Management System ---
You stop falling through time, catch your breath, and check the screen on the device. "Destination reached. Current Year: 1518. Current Location: North Pole Utility Closet 83N10." You made it! Now, to find those anomalies.

Outside the utility closet, you hear footsteps and a voice. "...I'm not sure either. But now that so many people have chimneys, maybe he could sneak in that way?" Another voice responds, "Actually, we've been working on a new kind of suit that would let him fit through tight spaces like that. But, I heard that a few days ago, they lost the prototype fabric, the design plans, everything! Nobody on the team can even seem to remember important details of the project!"

"Wouldn't they have had enough fabric to fill several boxes in the warehouse? They'd be stored together, so the box IDs should be similar. Too bad it would take forever to search the warehouse for two similar box IDs..." They walk too far away to hear any more.

Late at night, you sneak to the warehouse - who knows what kinds of paradoxes you could cause if you were discovered - and use your fancy wrist device to quickly scan every box and produce a list of the likely candidates (your puzzle input).

To make sure you didn't miss any, you scan the likely candidate boxes again, counting the number that have an ID containing exactly two of any letter and then separately counting those with exactly three of any letter. You can multiply those two counts together to get a rudimentary checksum and compare it to what your device predicts.

For example, if you see the following box IDs:

abcdef contains no letters that appear exactly two or three times.
bababc contains two a and three b, so it counts for both.
abbcde contains two b, but no letter appears exactly three times.
abcccd contains three c, but no letter appears exactly two times.
aabcdd contains two a and two d, but it only counts once.
abcdee contains two e.
ababab contains three a and three b, but it only counts once.
Of these box IDs, four of them contain a letter which appears exactly twice, and three of them contain a letter which appears exactly three times. Multiplying these together produces a checksum of 4 * 3 = 12.

What is the checksum for your list of box IDs?

Your puzzle answer was 8892.

--- Part Two ---
Confident that your list of box IDs is complete, you're ready to find the boxes full of prototype fabric.

The boxes will have IDs which differ by exactly one character at the same position in both strings. For example, given the following box IDs:

abcde
fghij
klmno
pqrst
fguij
axcye
wvxyz
The IDs abcde and axcye are close, but they differ by two characters (the second and fourth). However, the IDs fghij and fguij differ by exactly one character, the third (h and u). Those must be the correct boxes.

What letters are common between the two correct box IDs? (In the example above, this is found by removing the differing character from either ID, producing fgij.)
*/

class Day02 extends FlatSpec {
  val input = """zihrtxagncfpbsnolxydujjmqv
                |zihrtxagwcfpbsoolnydukjyqv
                |aihrtxagwcfpbsnoleybmkjmqv
                |zihrtxagwcfpbsnolgyduajmrv
                |zihrtxgmwcfpbunoleydukjmqv
                |zihqtxagwcfpbsnolesdukomqv
                |zihgtxagwcfpbsnoleydqkjqqv
                |dihrtxagwcqpbsnoleydpkjmqv
                |qihrtvagwcfpbsnollydukjmqv
                |zihrtgagwcfpbknoleyrukjmqv
                |cinrtxagwcfpbsnoleydukjaqv
                |zihrtxagwcfubsneleyvukjmqv
                |zihrtxagwcfpbsvoleydukvmtv
                |zihrtpagwcffbsnolfydukjmqv
                |zihrtxagwcfpbsxoleydtkjyqv
                |zohrvxugwcfpbsnoleydukjmqv
                |zyhrtxagdcfpbsnodeydukjmqv
                |zihrtxaghffpbsnoleyduojmqv
                |oihrtbagwcfpbsnoleyduejmqv
                |zihrtnagwcvpjsnoleydukjmqv
                |iihrtxagwcfpbsnoliyaukjmqv
                |ziartxagwcfpbsnokeydukjmpv
                |eibrtxagwccpbsnoleydukjmqv
                |zihrtxagwczwbsaoleydukjmqv
                |ziiatuagwcfpbsnoleydukjmqv
                |zzhrtxagwckpbsnsleydukjmqv
                |cihrtxaqwcfpbsnoleydkkjmqv
                |zihrtxaywcfpbsnoleydukzdqv
                |zihrtxagwjfpbvnoleydukjmql
                |zihrtxagwcfpbsnoleuduksmql
                |zizrtxxgwcfpbsnoleydukzmqv
                |zihrteagwcfpbsnobeydukjmqe
                |zihrtxafwhfpbsgoleydukjmqv
                |zitrtxagwcfpbsnoleyduvymqv
                |zihrtxauwcfebsnoleygukjmqv
                |zihrtxagwcfpbsnoleydubjrqh
                |zihrtxauwmfpbsnoleydukjmqo
                |zihrtxagwcdpbsnoleydukxmov
                |zihrtmagwcfpbsnoleydukvmlv
                |ziwrtxhgwcfpbsnoleodukjmqv
                |zihytxagacfpbsnoceydukjmqv
                |zihrtxagwcfpbsnolebdugjnqv
                |zihrzxagwcfpbsnjleyduktmqv
                |zihrtxygwcfpbinoleysukjmqv
                |zihrtxagwcfpbmnoveydujjmqv
                |zidrtxagwcfpbsnolexaukjmqv
                |zshrtxagwcepbsnoxeydukjmqv
                |yibrtxagwzfpbsnoleydukjmqv
                |zehrtxagwclpbsnoleymukjmqv
                |zihruxagwcfpbsnoleyhukwmqv
                |zihrwxagwcfpbszolesdukjmqv
                |zihrtpagwcfpbwnoleyuukjmqv
                |ziortxagwcfpssnolewdukjmqv
                |zohrtxagwcfpbwnoleydukjmjv
                |zihrtxagwcfpbsnvleyduzcmqv
                |zihrvxaghcfpbswoleydukjmqv
                |zihrtxagwcfpssnolwydukzmqv
                |zjhrttagwcfpbsnolfydukjmqv
                |zihrtxagwjfpbsnoljydukpmqv
                |ziwrtxagwczpbsnoljydukjmqv
                |zinrtxagwcfpbvfoleydukjmqv
                |zihrgragwcfpbsnoleydutjmqv
                |zihrtxagwcfpbsnozeydukffqv
                |zihrtxagwcfpbsmoleydxkumqv
                |rihwtxagwcfpbsxoleydukjmqv
                |ziqrtxagwcfpbsnqlevdukjmqv
                |zihrtxagwchpbsnoleydufamqv
                |sihrtxagwcfpbsnoleldukjmqp
                |zihrtxagwcrpbsnoleydvojmqv
                |zihrtxacwcfpbsnoweyxukjmqv
                |zihrtxagwcfpbsnolajmukjmqv
                |zzfrtxagwcfpbsnoleydukjmvv
                |zixrtxagwcfpbqnoleydukjgqv
                |zihitxaqwcfpbsnoleadukjmqv
                |zilrtxagecfxbsnoleydukjmqv
                |zihrtxagwcfpbypoleycukjmqv
                |zidrtxagdtfpbsnoleydukjmqv
                |lehrtxagxcfpbsnoleydukjmqv
                |zihrlxagwcfpbsncneydukjmqv
                |zihroxagbcspbsnoleydukjmqv
                |zihrtxagwcfkzsnolemdukjmqv
                |zihrtxagwcfpbsqeleydukkmqv
                |zihrjxagwcfpesnolxydukjmqv
                |zifrtxagwcfpbsooleydukkmqv
                |zirwtxagwcfpbsnoleydukzmqv
                |zjhntxagwcfpbsnoleydunjmqv
                |ziorexagwcfpbsnoyeydukjmqv
                |zhhrtlagwcfybsnoleydukjmqv
                |zirrtxagwvfsbsnoleydukjmqv
                |bihrtxagwofpbsnoleadukjmqv
                |dihrtxagwcfpksnoleydukjlqv
                |zihrrxagecfpbsnoleydukjmyv
                |zijrtxagwmfpbsnoleyduljmqv
                |zihrtxagwcfpbsnolecdukjpqs
                |zchrtxagwcfpbsnolehdukjmwv
                |rmhrtxagwcfpbsnoleydkkjmqv
                |zohrotagwcfpbsnoleydukjmqv
                |zihwtxagsifpbsnwleydukjmqv
                |zihrtxagicfpbsnoleydukjxqn
                |zihrtxsgwcfpbsntleydumjmqv
                |zihrlxagzgfpbsnoleydukjmqv
                |aihjtxagwdfpbsnoleydukjmqv
                |zifrtxagwcfhbsnoleddukjmqv
                |zihrtyagwcfpbsooleydtkjmqv
                |zihrtxxgwcfpbsnolerhukjmqv
                |zihqtxalwcfppsnoleydukjmqv
                |zfkrvxagwcfpbsnoleydukjmqv
                |zihptxagwcfpbseoleydukjmdv
                |zihrtxagwcfpeonoleyiukjmqv
                |nidrtxagwcfpbsnoleyhukjmqv
                |zihrtxagwcfjbsnolsydukjmqg
                |zghryxagwcfgbsnoleydukjmqv
                |zihwtxagwcfpbsnoleydugjfqv
                |zihryxagwjfpbsnoleydujjmqv
                |zihrtxagwcfpbsnolekdukymql
                |zfhrtxaownfpbsnoleydukjmqv
                |zamrtxagwcfpbsnoleyduzjmqv
                |ibhrtxagwcfpbsnoleydukjmfv
                |zihrtxagwcfpssnoseydukjmuv
                |zihrtxagwcfpbsnoljydukjhqs
                |zihrtxagwqfmbsnoleidukjmqv
                |zfdrtxagwchpbsnoleydukjmqv
                |iihrtxagqcfpbsnoleydukjmqn
                |mihrtxagwcfpbsqoleydukjbqv
                |zihttxagwcfpbsnoleyduljmqk
                |zzhrtxagwcfpzseoleydukjmqv
                |zdhrtxagbcfpbsnoleyduyjmqv
                |zihxtxagwcfpbsnolwrdukjmqv
                |zghrtxagwcypbynoleydukjmqv
                |zihrtxaiwcfppsnoleydukgmqv
                |zitatxagwcfobsnoleydukjmqv
                |znhrtxagwcfpysnoleydukjqqv
                |zihrtxagwcfppsnoleoyukjmqv
                |ziorgxagwcfpbsnolekdukjmqv
                |zihrtxagwcfpbfnoleydwkjpqv
                |zihrtxnrwcfpbsnolnydukjmqv
                |rihrtxagwcfpbsnolepdjkjmqv
                |zihrtxagwcfzbsnoceydukjmkv
                |zihrtxagwcfpysnoaeidukjmqv
                |zihrmxagwcfpbsnoleydukjmuq
                |gihrtxagwcvpbsnoleydukcmqv
                |zihrtxagocfpbsnoleydukqmnv
                |zihrtxagwcfpesnoleyluklmqv
                |zghrtxagwcfzbsnoleydukjmgv
                |zihrtxugqqfpbsnoleydukjmqv
                |zirrtcagwcfpbsnoleydfkjmqv
                |zihitxagwcfpjsnoleydnkjmqv
                |zihrtxqgwcfpbsnsleydukjmqy
                |iihrtxagwyfpbsnoleydukjmqu
                |zihrsxagwcfpbsnsleydukzmqv
                |zihrtxawwcfpbsnoleydzkjmuv
                |dihrkxagwcfpbsfoleydukjmqv
                |zihrtxaqwcfpbvnoleydukjmqt
                |zihntxdgwcfpbsnogeydukjmqv
                |zihrtxagwcdpxsnolxydukjmqv
                |zihrtxagwcfpbsaoleydunjaqv
                |zihrtyagwcfpbsnoleyduqjmqt
                |zihrtxagwtfpbsnoleoyukjmqv
                |zihrjiagwcfpbsnobeydukjmqv
                |zihrtxqgwcfpbsnoleydykdmqv
                |zihrhxmgwcfpbsnmleydukjmqv
                |zihatxlgwcfpbsnoleydukpmqv
                |zihrtxcgwcspbsnoleypukjmqv
                |zihrtkagqcfpbsaoleydukjmqv
                |ziqrtxagwcfabsnoleydukrmqv
                |zihwtxagwifpbsnwleydukjmqv
                |zitrtnagwcfpbsnoleddukjmqv
                |wihrtxagwcfpbsioyeydukjmqv
                |zihrtxagwclpystoleydukjmqv
                |zihmtxagwcfpbsnolfydukjmlv
                |zihrtxagechpbsnoleydutjmqv
                |zihrtxagwcfebsnolnydukjmuv
                |zihrtxagncmpbsnoleydukjmqs
                |zihrvxagocfpbsnoleydukcmqv
                |zihrtxagwcjcbsnolejdukjmqv
                |wihrtxagwcfpbogoleydukjmqv
                |kivrtxagwcfpgsnoleydukjmqv
                |zihrtxagwafpbhnoleydukjcqv
                |zihrtwagtcfpbsnolxydukjmqv
                |vihrtxagwcfpbsneletdukjmqv
                |zihlnxagwcfpbsnoleydukjmqb
                |zihrtxagwcfpbsnoleydukjuuc
                |zihrtxagwcfpbwntleadukjmqv
                |fihrtxagwcfpbsnoleydvkjmqw
                |zihrtxaowcfpbunoleyduljmqv
                |zthrtxagwcfpbtnoleydukomqv
                |xihltxagwcfpbsnoleydukjrqv
                |ziyrnxagwcfpbsnoleydukjmhv
                |zihrtxazwcfpbsnileyduejmqv
                |zihrtxagwcfibsnoliydukjmsv
                |zihrtxggwcfpbsnoleydugjmqj
                |zrartxagwcffbsnoleydukjmqv
                |zidrtxaqwcfpbsnoleyduksmqv
                |zirrtxagwcypbsnoleydtkjmqv
                |rihrtxagwcrpbsnoheydukjmqv
                |zihrtxagwcfpbsnoleydpkjmzs
                |zihrtxagbcfpbsnodbydukjmqv
                |fihrtxaqwcfpbsnolaydukjmqv
                |vihrtxbgwcfpbsnolemdukjmqv
                |zihrtxapwcfubsnoleydukmmqv
                |zihrtxagwcfpbgnolfydunjmqv
                |zihrtxagwcypbsnokeyduvjmqv
                |zihntxagwcfpbsnoieydukbmqv
                |zihbtxagwkfpbsnolpydukjmqv
                |zihrtxagwcfibsnoleydikjmqb
                |jihrtxvgwcfpbsnoleydukjmqp
                |zihrtxagwcfpbjnqleydukjmlv
                |zibrtxagwcfpbzvoleydukjmqv
                |zihrtxagwafgbsnbleydukjmqv
                |zihjctagwcfpbsnoleydukjmqv
                |zahrtxagwcepbsnoleddukjmqv
                |zihetxagwcfpbsnoleydumjmsv
                |zihrtvagwcfpbbnoleydukdmqv
                |zbhrxxagwkfpbsnoleydukjmqv
                |jfhrtxagwcftbsnoleydukjmqv
                |yihrtxagwcfvbsnoleyduksmqv
                |ziartxaewcfpbsnoleyduhjmqv
                |zihrtxagwcfpbsnoozyduzjmqv
                |cihotxagwcfpysnoleydukjmqv
                |zihrtxagwcfpusnolwydxkjmqv
                |zihrtxagwcfpbsnoleedmgjmqv
                |zihrtxaghcfpmsnoleydukqmqv
                |ziortxagwcfpbsboleidukjmqv
                |zihrtxagwcfybsnoleyqxkjmqv
                |zihrtxamwcfpbsngleydukjmqx
                |zihrtxagwcfpbsnoleyduusmqu
                |zihftxagwcfpssnwleydukjmqv
                |zihrtxagwcfkbsnomeydukjmsv
                |zihrtxagwcvpbsnooeydwkjmqv
                |zihrtxagwcfpbsnoleycekumqv
                |jahrtxagwcfpbsnoleydukjmmv
                |zihrtxabwcfpbsnzheydukjmqv
                |zihrtxagwctpbsnoleydwkjmhv
                |zihrtpagwcfpbsnoleydzkjmqh
                |zihwtxagwcfpbsnollydukjrqv
                |zihrtxagwcfpusnoleydsvjmqv
                |zibrtxagwcfpasnoleydukjmbv
                |zchrtmagwcfpbsnoleydukjmwv
                |ziertxbgwyfpbsnoleydukjmqv
                |zitrtxagwcfpbhnoweydukjmqv
                |zisrtxkgwcfpbsnopeydukjmqv
                |zihrtxcgwdfpbynoleydukjmqv
                |iihrtxajwcvpbsnoleydukjmqv
                |zihuwxapwcfpbsnoleydukjmqv
                |zihrtxngwcfqbsnoleyiukjmqv
                |ziqrtxagjcfpbsnoleydukjmqi
                |zifrtxarwctpbsnoleydukjmqv
                |zihxgxagwcfpbpnoleydukjmqv
                |giprtxagwcdpbsnoleydukjmqv
                |zihrtxagwmfpbsnodeydukjbqv""".stripMargin

  // Ugly first solution
  def part1(s: String) = {
    val t = s.split("\n").map { b =>
      val c = b.split("")
      val cs = c.groupBy(identity).mapValues(_.size)
      val h2 = cs.exists(_._2 == 2)
      val h3 = cs.exists(_._2 == 3)
      (h2, h3)
    }
    t.filter(_._1).size * t.filter(_._2).size
  }

  // Cleaned up solution
  def part1a(s: String) = {
    val c = s.split("\n").map(_.split("").groupBy(identity).mapValues(_.size))
    c.count(_.exists(_._2 == 2)) * c.count(_.exists(_._2 == 3))
  }

  "part1" should "satisfy the examples given" in {
    assertResult(12)(part1("""abcdef
                             |bababc
                             |abbcde
                             |abcccd
                             |aabcdd
                             |abcdee
                             |ababab""".stripMargin))
  }

  "part1" should "succeed" in { info(part1(input).toString) }

  // Ugly first solution
  def part2(s: String) = {
    val a = s.split("\n").map(_.split(""))
    def c(a: Seq[String], b: Seq[String]) = {
      val z = a.zip(b)
      val d = z.map(_ match { case (x, y) => if (x != y) None else Some(x) })
      if (d.count(_ == None) == 1) Some(d.filter(_ != None).map(_.get).mkString) else None
    }
    a.flatMap{ x => a.map(y => c(x, y)).filter(_ != None) }.head.get
  }

  // Streaming solution
  def part2a(s: String) = {
    val a = s.split("\n").map(_.split("")).toStream
    def f(a: Seq[String], b: Seq[String]) = {
      val (c, s) = a.zip(b).foldLeft((0, "")) { case ((c, s), (x, y)) => if (x == y) (c, s + x) else (c + 1, s) };
      if (c == 1) Some(s) else None
    }
    a.zip(a.tail.tails.toStream).flatMap { case (x, ys) => ys.map(f(x, _)) }.filter(_ != None).head.get
  }

  "part2" should "satisfy the examples given" in {
    assertResult("fgij")(part2("""abcde
                                 |fghij
                                 |klmno
                                 |pqrst
                                 |fguij
                                 |axcye
                                 |wvxyz""".stripMargin))

  }

  "part2" should "succeed" in { info(part2(input).toString) }
}
