package com.ctc.daffodil.ext

import java.io.InputStream
import java.nio.file.Paths

import com.helger.schematron.svrl.jaxb.SchematronOutputType
import com.helger.schematron.xslt.SchematronResourceXSLT
import org.apache.daffodil.api.Validator
import org.xml.sax.ErrorHandler

class SchematronValidator extends Validator {
  override def name(): String = "sch"

  override def checkArgs(args: Validator.Arguments): Either[String, Unit] = args match {
    case Seq() => Left("Path to a Schematron schema required.")
    case s: Seq[_] if s.exists(schemaArg) => Right(Unit)
    case v => Left(s"Unsupported arguments, $v")
  }

  def validateXML(document: InputStream, errHandler: ErrorHandler, args: Seq[Validator.Argument]): Unit = {
    args.find(schemaArg) match {
      case Some(Validator.Argument(_, path)) =>
        val res = SchematronResourceXSLT.fromFile(Paths.get(path).toFile)
        if(!res.isValidSchematron)
          throw new Exception("invalid schematron")
        val svrl: SchematronOutputType = res.applySchematronValidationToSVRL(SchUtil.istream(document))
        SchUtil.printSvrl(svrl)
      case None =>
        println("KaBoom!")
    }
  }

  private def schemaArg(arg: Validator.Argument): Boolean = arg.key == "default" || arg.key == "schema"
}
