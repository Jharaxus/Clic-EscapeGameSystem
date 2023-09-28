package clic

object AnsiColors {
  // Reset
  val Color_Off="\u001b[0m" // Text Reset

// Regular Colors
val Regular_Black="\u001b[0;30m"        // Black
val Regular_Red="\u001b[0;31m"          // Red
val Regular_Green="\u001b[0;32m"        // Green
val Regular_Yellow="\u001b[0;33m"       // Yellow
val Regular_Blue="\u001b[0;34m"         // Blue
val Regular_Purple="\u001b[0;35m"       // Purple
val Regular_Cyan="\u001b[0;36m"         // Cyan
val Regular_White="\u001b[0;37m"        // White

// Background
val Background_On_Black="\u001b[40m"       // Black
val Background_On_Grey="\u001b[47m"       // Black
val Background_On_Red="\u001b[101m"         // Red
val Background_On_Green="\u001b[42m"       // Green
val Background_On_Yellow="\u001b[43m"      // Yellow
val Background_On_Blue="\u001b[44m"        // Blue
val Background_On_Purple="\u001b[45m"      // Purple
val Background_On_Cyan="\u001b[46m"        // Cyan
val Background_On_White="\u001b[107m"       // White

  val Background_On_Grey_FG_Black ="\u001b[30;101m"
}
