package com.ctc.daffodil.ext

import java.io.{ByteArrayOutputStream, InputStream}
import java.nio.charset.StandardCharsets

import com.helger.commons.io.IHasInputStream
import com.helger.schematron.svrl.{SVRLMarshaller, SVRLNamespaceContext}
import com.helger.schematron.svrl.jaxb.SchematronOutputType

object SchUtil {
  def printSvrl(svrl: SchematronOutputType): Unit = {
    val aMarshaller = new SVRLMarshaller(false)
    aMarshaller.setFormattedOutput(true)
    aMarshaller.setNamespaceContext(SVRLNamespaceContext.getInstance)

    val bos = new ByteArrayOutputStream()
    aMarshaller.write(svrl, bos)
    val txt = bos.toString
    println(txt)
  }

  def istream(is: InputStream): IHasInputStream = new IHasInputStream {
    def getInputStream: InputStream = is

    def isReadMultiple: Boolean = false
  }
}
