package clic

object System {
  def main(args: Array[String]): Unit = {
    args(0) match {
      case Config.CountdownArg => CountdownSystem.launch(2, 30)
      case Config.InterfaceArg => UserInterface.launch()
    }
  }
}
