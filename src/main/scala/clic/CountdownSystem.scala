package clic

import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.collection.mutable

object CountdownSystem {

  private val numbers: HashMap[Int, List[String]] = HashMap(
    0 -> List(
      "▄▄▄▄▄",
      "█░▄░█",
      "█░█░█",
      "█░▀░█",
      "▀▀▀▀▀"),
    1 -> List(
      "▄▄▄▄▄",
      "█▀░██",
      "██░██",
      "█▀░▀█",
      "▀▀▀▀▀"),
    2 -> List(
      "▄▄▄▄▄",
      "█░▄░█",
      "██▀▄█",
      "█░▀▀█",
      "▀▀▀▀▀"),
    3 -> List(
      "▄▄▄▄▄▄",
      "█░▄▄░█",
      "███▄▀█",
      "█░▀▀░█",
      "▀▀▀▀▀▀"),
    4 -> List(
      "▄▄▄▄▄▄▄",
      "██░▄░██",
      "█░▀▀░▀█",
      "████░██",
      "▀▀▀▀▀▀▀"),
    5 -> List(
      "▄▄▄▄▄",
      "█░▄▄█",
      "█▄▄▀█",
      "█▀▀▄█",
      "▀▀▀▀▀"),
    6 -> List(
      "▄▄▄▄▄▄",
      "█▀▄▄▀█",
      "█░▀▀██",
      "█▄▀▀▄█",
      "▀▀▀▀▀▀"),
    7 -> List(
      "▄▄▄▄▄▄",
      "█▄▄▄░█",
      "███░██",
      "██▌▐██",
      "▀▀▀▀▀▀"),
    8 -> List(
      "▄▄▄▄▄▄",
      "█▀▄▄▀█",
      "█▀▄▄▀█",
      "█▄▀▀▄█",
      "▀▀▀▀▀▀"),
    9 -> List(
      "▄▄▄▄▄▄",
      "█▀▄▄▀█",
      "█▄▀▀░█",
      "██▀▀▄█",
      "▀▀▀▀▀▀")
  )

  private val separator: List[String] = List(
    "▄▄▄",
    "█▀█",
    "███",
    "█▄█",
    "▀▀▀")

  private def printTime(minutes: Int, seconds: Int): Unit = {
    val elements: List[List[String]] = List(
      numbers(minutes / 10),
      numbers(minutes % 10),
      separator,
      numbers(seconds / 10),
      numbers(seconds % 10)
    )

    (0 to 4).foreach(n => {
      elements.foreach(l =>
        l.drop(n).head.foreach(print(_)))
      println("")
    })
  }

  @tailrec
  private def countdown(minutes: Int, seconds: Int): Unit = {
    if (minutes == 0 && seconds == 0) {
      println("Mission failed !!!!!")
      return
    }
    print("\n\n\n\n\n\n\n\n\n\n\n")
    println("-- System Andromeda --")
    printTime(minutes, seconds)
    Thread.sleep(1000)

    val nSeconds = if (seconds == 0) 60 else seconds - 1
    val nMinutes = if (seconds == 60) minutes - 1 else minutes

    countdown(nMinutes, nSeconds)
  }

  def launch(minutes: Int, seconds: Int): Unit = {
    countdown(minutes, seconds)
  }
}
