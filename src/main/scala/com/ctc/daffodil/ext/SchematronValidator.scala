package com.ctc.daffodil.ext

import java.io.InputStream

import com.helger.schematron.svrl.jaxb.{FailedAssert, SchematronOutputType}
import com.helger.schematron.xslt.SchematronResourceXSLT
import org.apache.daffodil.api.{ValidationError, ValidationResult, Validator}

import scala.collection.JavaConverters._

case class SchematronValidationError(f: FailedAssert) extends ValidationError {
  def message: String = f.getText.toString
  def toThrowable: Throwable = new Exception(f.getText.toString)
}

class SchematronValidator(sch: SchematronResourceXSLT) extends Validator {
  def validateXML(document: InputStream): ValidationResult = {
    val svrl: SchematronOutputType = sch.applySchematronValidationToSVRL(SchUtil.istream(document))
    val errors = svrl.getActivePatternAndFiredRuleAndFailedAssert.asScala.flatMap {
      case f: FailedAssert => Some(SchematronValidationError(f))
      case _               => None
    }
    ValidationResult(Seq.empty, errors)
  }
}
