package retronym.lessons.collections

import _root_.org.spex.Specification

object ForComprehensions extends Specification {
  val weekDays = List("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
  val daySections = List("morning", "afternoon", "evening")
  "ForComprehensions" should {
    "should allow map, filter of collection" in {
      val a = for {w <- weekDays if w startsWith "W"
        wu = w.toUpperCase
      } yield wu
      a must containAll(List("WEDNESDAY"))
    }
    "List of Lists" in {
      val a = for {w <- weekDays if w startsWith "T"
        weekdaysUpperCaseLetters = w.toList
      } yield weekdaysUpperCaseLetters
      a must containAll(List('T', 'u', 'e', 's', 'd', 'a', 'y'), List('T', 'h', 'u', 'r', 's', 'd', 'a', 'y'))
    }

    "Same as 'List of Lists', but without the sugar" in {
      val a = weekDays.filter(_ startsWith "T").map(_.toList)
      a must containAll(List('T', 'u', 'e', 's', 'd', 'a', 'y'), List('T', 'h', 'u', 'r', 's', 'd', 'a', 'y'))
    }
    
    "Flat list by using <- operator twice" in {
      val a = for {w <- weekDays if w startsWith "T"
        weekdaysUpperCaseLetters <- w
      } yield weekdaysUpperCaseLetters
      a must containAll(List('T', 'u', 'e', 's', 'd', 'a', 'y', 'T', 'h', 'u', 'r', 's', 'd', 'a', 'y'))
    }

    "Same as 'Flat list by using <- operator twice', but without the sugar" in {
      val a = weekDays.filter(_ startsWith "T").flatMap(_.toList)
      a must containAll(List('T', 'u', 'e', 's', 'd', 'a', 'y', 'T', 'h', 'u', 'r', 's', 'd', 'a', 'y'))
    }
  }
}
