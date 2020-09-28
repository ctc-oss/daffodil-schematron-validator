package com.ctc.daffodil.ext

import java.io.{ByteArrayOutputStream, File, InputStream}

import com.helger.commons.io.IHasInputStream
import com.helger.commons.io.resource.FileSystemResource
import com.helger.commons.state.ESuccess
import com.helger.schematron.svrl.jaxb.SchematronOutputType
import com.helger.schematron.svrl.{CSVRL, SVRLMarshaller, SVRLNamespaceContext}
import com.helger.schematron.xslt.{SCHTransformerCustomizer, SchematronResourceSCHCache}
import com.helger.xml.XMLHelper
import com.helger.xml.namespace.MapBasedNamespaceContext
import com.helger.xml.serialize.write.{XMLWriter, XMLWriterSettings}
import javax.xml.XMLConstants

object SchUtil {
  def sch2xslt(sch: File): Either[String, String] = {
    val res = new FileSystemResource(sch.getAbsolutePath)
    val xsltProvider = SchematronResourceSCHCache.createSchematronXSLTProvider(res, new SCHTransformerCustomizer())
    val ns = new MapBasedNamespaceContext().addMapping("svrl", CSVRL.SVRL_NAMESPACE_URI)
    val nsPrefix = XMLConstants.XMLNS_ATTRIBUTE + ":"
    XMLHelper.forAllAttributes(
      xsltProvider.getXSLTDocument.getDocumentElement,
      (k: String, v: String) => if (k.startsWith(nsPrefix)) ns.addMapping(k.substring(nsPrefix.length), v)
    )

    val os = new ByteArrayOutputStream()
    val conf = new XMLWriterSettings().setNamespaceContext(ns).setPutNamespaceContextPrefixesInRoot(true)
    XMLWriter.writeToStream(xsltProvider.getXSLTDocument, os, conf) match {
      case ESuccess.SUCCESS => Right(os.toString)
      case ESuccess.FAILURE => Left("Failed to convert SCH to XSLT.")
    }
  }

  def printSvrl(svrl: SchematronOutputType): Unit = {
    val marshaller = new SVRLMarshaller(false)
    marshaller.setFormattedOutput(true)
    marshaller.setNamespaceContext(SVRLNamespaceContext.getInstance)

    val bos = new ByteArrayOutputStream()
    marshaller.write(svrl, bos)
    val txt = bos.toString
    println(txt)
  }

  def istream(is: InputStream): IHasInputStream = new IHasInputStream {
    def getInputStream: InputStream = is

    def isReadMultiple: Boolean = false
  }
}
