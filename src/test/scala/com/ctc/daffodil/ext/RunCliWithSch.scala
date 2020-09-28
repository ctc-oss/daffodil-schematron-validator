package com.ctc.daffodil.ext

import org.apache.daffodil.Main

object RunCliWithSch extends App {
  val cliArgs = Array(
    "parse",
    "--schema",
    "data/bmp.dfdl.xsd",
    "--validate",
    "sch=data/bmp.sch",
    "data/MARBLES.BMP"
  )

  Main.main(cliArgs)
}
