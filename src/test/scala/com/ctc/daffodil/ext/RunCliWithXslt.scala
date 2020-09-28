package com.ctc.daffodil.ext

import org.apache.daffodil.Main

object RunCliWithXslt extends App {
  val cliArgs = Array(
    "parse",
    "--schema",
    "data/bmp.dfdl.xsd",
    "--validate",
    "sch=data/bmp.xslt",
    "data/MARBLES.BMP"
  )

  Main.main(cliArgs)
}
