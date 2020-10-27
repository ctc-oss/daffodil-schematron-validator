package com.ctc.daffodil.ext

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.helger.schematron.xslt.SchematronResourceXSLT
import org.scalatest.{Matchers, WordSpec}


class ConvertSch extends WordSpec with Matchers {
  "sch" should {
    "convert to xslt" in {
      val xslt = SchUtil.sch2xslt(Paths.get("data/bmp.sch").toFile)
      val res = SchematronResourceXSLT.fromString(xslt, StandardCharsets.UTF_8)
      println(xslt)
      println(res.isValidSchematron)
    }
  }
}
