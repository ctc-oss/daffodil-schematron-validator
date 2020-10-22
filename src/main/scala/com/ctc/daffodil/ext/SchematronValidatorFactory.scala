package com.ctc.daffodil.ext

import java.nio.file.{Files, Paths}

import com.helger.schematron.xslt.SchematronResourceXSLT
import com.typesafe.config.Config
import org.apache.daffodil.api.{Validator, ValidatorFactory, ValidatorInitializationException}

class SchematronValidatorFactory extends ValidatorFactory {
  def name(): String = "schematron"

  def make(config: Option[Config]): Validator = {
    val sch = config match {
      case Some(cfg) if cfg.hasPath(name()) =>
        Paths.get(cfg.getString(name()))
      case _ =>
        throw ValidatorInitializationException("invalid configuration")
    }

    if (!Files.exists(sch))
      throw ValidatorInitializationException(s"schematron does not exist at $sch")

    val res = SchematronResourceXSLT.fromFile(sch.toFile);

    if (!res.isValidSchematron)
      throw new Exception("invalid schematron")

    new SchematronValidator(res)
  }
}
