package clic

object System {
  def main(args: Array[String]): Unit = {
//    print(args)
//    args(1) match {
//      case Config.CountdownArg => CountdownSystem.launch(2, 30)
//      case Config.InterfaceArg => UserInterface.launch()
//    }
    UserInterface.launch()
  }
}
