package com.ctc.daffodil.ext

import java.nio.file.{Files, Paths}

import com.helger.schematron.xslt.SchematronResourceXSLT
import com.typesafe.config.Config
import org.apache.daffodil.api.{Validator, ValidatorFactory, ValidatorInitializationException}

class SchematronValidatorFactory extends ValidatorFactory {
  def name(): String = "schematron"

  def make(config: Config): Validator = {
    if(!config.hasPath(name()))
      throw ValidatorInitializationException("invalid configuration")

    val sch = Paths.get(config.getString(name()))
    if (!Files.exists(sch))
      throw ValidatorInitializationException(s"schematron does not exist at $sch")

    val res = SchematronResourceXSLT.fromFile(sch.toFile);

    if (!res.isValidSchematron)
      throw new Exception("invalid schematron")

    new SchematronValidator(res)
  }
}
