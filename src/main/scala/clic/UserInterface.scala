package clic

import java.util.{Calendar, Date}
import scala.annotation.tailrec

sealed abstract class UserState(val expect: String, val onLand: () => Unit, val onTry: () => Unit,
                                val onFail: String => Unit, val onComplete: () => Unit)

object UserInterface {

  var lastLoginDate: Option[String] = None

  private def getTime(): String = {
    val date = Calendar.getInstance()
    val curr_Minute = date.get(Calendar.MINUTE)
    val curr_Hour = date.get(Calendar.HOUR_OF_DAY)

    if (curr_Hour > 12) {
      curr_Hour % 12 + ":" + curr_Minute + " PM"
    }
    else {
      curr_Hour + ":" + curr_Minute + " AM"
    }
  }

  private def printWait(waitingText: String, onCompleteText: String): Unit = {
    (0 until 6).foreach(_ => {
      print("\r" + waitingText + ".")
      Thread.sleep(200)
      print("\r" + waitingText + "..")
      Thread.sleep(200)
      print("\r" + waitingText + "...")
      Thread.sleep(200)
    })
    print("\r" + onCompleteText)
  }

  private def print2FA(): Unit = {
    val blackTile = AnsiColors.Background_On_Black + "   "
    val space = AnsiColors.Color_Off + " "
    val greenTile = AnsiColors.Background_On_Green + "   "
    val greyTile = AnsiColors.Background_On_Grey + "   "
    val yellowTile = AnsiColors.Background_On_Yellow + "   "
    val whiteTile = AnsiColors.Background_On_White + "   "
    val blueTile = AnsiColors.Background_On_Blue + "   "
    val interrogationMark =
      AnsiColors.Background_On_Red + " " + AnsiColors.Background_On_Grey_FG_Black + "?" + AnsiColors.Background_On_Red + " "

    @tailrec
    def printTiles(tiles: List[String]): Unit = {
      if (tiles.isEmpty) return
      print(tiles.head)
      print(AnsiColors.Color_Off)
      if (tiles.head.isEmpty) return
      print(AnsiColors.Color_Off)
      printTiles(tiles.tail)
    }

    printTiles(List(blackTile, blackTile, greenTile, blackTile, blackTile))
    print("\n")
    printTiles(List(blackTile, interrogationMark, interrogationMark, greyTile, blackTile))
    print("\n")
    printTiles(List(whiteTile, greyTile, interrogationMark, greyTile, yellowTile))
    print("\n")
    printTiles(List(blackTile, greyTile, greyTile, interrogationMark, blackTile))
    print("\n")
    printTiles(List(blackTile, blackTile, blueTile, blackTile, blackTile))
    print("\n")
  }

  final case object COUNTDOWN extends UserState(
    Config.AdminPassword,
    () => {
      print("-- System Terra --\n\n")
      print("Update ongoing as scheduled.\n")
    },
    () => print("Enter admin password to interrupt.\n> "),
    _ => print("Incorrect password.\nUpdate is resuming.\n"),
    () => {
      lastLoginDate = Some(getTime())
      printWait(
        "Admin password prompted, interrupting the update",
        "Update interrupted.\n"
      )
      Thread.sleep(2000)
    }
  )

  final case object START_TRANSFER extends UserState(
    Config.FundTransferOption,
    () => print("Maintenance interface loaded.\n\n"),
    () => print("What subsystem do you want to access ?\n" +
      "- 1: Query system state\n" +
      "- 2: Access login logs\n" +
      "- 3: Receive funds from external account\n" +
      "- 4: Transfer funds to external account\n" +
      "- 5: Start a global analysis\n" +
      "- 6: Restart update\n" +
      "> "),
    input => {
      if (input == "1") {
        print("System state: ongoing update and synchronization.")
        print("System upcoming state: unknown.")
      } else if (input == "2") {
        print(s"Last access: ${Config.AdminName} accessed with admin rights at ${if (lastLoginDate.isEmpty) "???" else lastLoginDate.get}.\n")
        print("Previous logs are not accessible during update.\n")
      } else if (input == "3") {
        print("Such operation is not permitted during update and synchronization.\n")
      } else if (input == "5") {
        print("Such operation is not permitted during update and synchronization.\n")
      } else if (input == "6") {
        print("This operation is not before at least 5 minutes of maintenance.\nTry again later.\n")
      } else {
        print("Unknown operation selected.\n")
      }
      Thread.sleep(2000)
      print("\n\n")
    },
    () => {
      print("Operation [Transfer funds to external account] selected.\n")
      print("Initiating funds transfer.\n")
    }
  )

  final case object TRANSFER_MDP extends UserState(
    Config.UserPassword,
    () => print(s"The user account selected is ${Config.Username}.\n"),
    () => print("To start the transfer, enter the current user account's password:\n> "),
    _ => print("Incorrect password.\nTry again.\n"),
    () => {
      print("Correct password.\n")
    }
  )

  final case object TRANSFER_2FA extends UserState(
    Config.Code2FA,
    () => {
      print("Starting 2FA authentication.\n")
      Thread.sleep(2000)
      printWait("Generating challenge", "Challenge generated.\n")
    },
    () => {
      print2FA()
      print("Enter the corresponding 4 digits code:\n> ")
    },
    _ => {
      print("Incorrect 2FA code.\n Try again.\n")
    },
    () => {
      print("Authentication successful.\n")
      print(s"Access granted to account ${Config.UserAccountNumber}.\n")
      print(s"Account balance: ${Config.FundAmount}\n")
    }
  )

  final case object TRANSFER_ADDRESS extends UserState(
    Config.AccountNumber,
    () => print("Select where the funds should be transferred.\n"),
    () => print("Write account name:\n> "),
    _ => print("This account does not exist.\nInput a valid account.\n"),
    () => print("Target account successfully selected.\n")
  )

  final case object VALIDATE_TRANSFER extends UserState(
    Config.Confirm,
    () => {
      print("Summary of the transfer:\n")
      print(s"user: ${Config.Username}\n")
      print(s"from: ${Config.UserAccountNumber}\n")
      print(s"to: ${Config.AccountNumber}\n")
      print(s"amount: ${Config.FundAmount}\n\n")
    },
    () => print("Write <CONFIRM> to confirm the transfer.\n> "),
    _ => print("Invalid confirmation.\n"),
    () => {
      print("Transfer confirmed.")
      Thread.sleep(2000)
      printWait("Processing transfer", "Transfer completed.")
      Thread.sleep(2000)
      print("Operation [Transfer funds to external account] terminated successfully.\n")
      print("Back to maintenance interface.\n")
    }
  )

  final case object RESTART extends UserState(
    Config.RestartOption,
    () => print("Maintenance interface loaded.\n\n"),
    () => print("What subsystem do you want to access ?\n" +
      "- 1: Query system state\n" +
      "- 2: Access login logs\n" +
      "- 3: Receive funds from external account\n" +
      "- 4: Transfer funds to external account\n" +
      "- 5: Start a global analysis\n" +
      "- 6: Restart update\n" +
      "> "),
    input => {
      if (input == "1") {
        print("System undergoing update and global synchronization.")
      } else if (input == "2") {
        print(s"Last access: ${Config.AdminName} accessed with admin rights at ${if (lastLoginDate.isEmpty) "???" else lastLoginDate.get}.\n")
        print("Previous logs are not accessible during update.\n")
      } else if (input == "3") {
        print("Such operation is not permitted during update and synchronization.\n")
      } else if (input == "4") {
          print("This operation may not be done twice in the same maintenance session.\n")
      } else if (input == "5") {
        print("Such operation is not permitted during update and synchronization.\n")
      } else {
        print("Unknown operation selected.\n")
      }
      Thread.sleep(2000)
      print("\n\n")
    },
    () => {
      print("Update successfully restarted.\n")
    }
  )

  private val statesFlow = List(COUNTDOWN, START_TRANSFER, TRANSFER_MDP, TRANSFER_2FA, TRANSFER_ADDRESS, VALIDATE_TRANSFER, RESTART)

  private def computeState(state: UserState): Unit = {
    state.onLand()
    Thread.sleep(1000)
    state.onTry()
    var read = scala.io.StdIn.readLine()
    while (!read.equals(state.expect)) {
      state.onFail(read)
      print("<" + read + ">")
      print("<" + state.expect + ">\n")
      Thread.sleep(1000)
      state.onTry()
      read = scala.io.StdIn.readLine()
    }
    state.onComplete()
    Thread.sleep(1000)
    print("\n")
  }

  def launch(): Unit = {
    for {
      state <- statesFlow
    } yield computeState(state)

    while(true)
      printWait("", "")
  }
}
