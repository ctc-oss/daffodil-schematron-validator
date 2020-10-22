package com.ctc.daffodil.ext

import java.io.InputStream

import com.helger.schematron.svrl.jaxb.{FailedAssert, SchematronOutputType}
import com.helger.schematron.xslt.SchematronResourceXSLT
import org.apache.daffodil.api.Validator
import org.xml.sax.ErrorHandler

import scala.collection.JavaConverters._

class SchematronValidator(sch: SchematronResourceXSLT) extends Validator {
  def validateXML(document: InputStream, errHandler: ErrorHandler): Unit = {
    val svrl: SchematronOutputType = sch.applySchematronValidationToSVRL(SchUtil.istream(document))
    svrl.getActivePatternAndFiredRuleAndFailedAssert.asScala.foreach {
      case f: FailedAssert =>
        // todo;; return as validation errors
    }
    SchUtil.printSvrl(svrl)
  }
}
