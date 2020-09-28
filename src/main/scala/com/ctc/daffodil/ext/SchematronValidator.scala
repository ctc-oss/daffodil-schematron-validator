package com.ctc.daffodil.ext

import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.helger.schematron.svrl.jaxb.SchematronOutputType
import com.helger.schematron.xslt.SchematronResourceXSLT
import org.apache.daffodil.api.Validator
import org.xml.sax.ErrorHandler

import scala.io.Source

class SchematronValidator extends Validator {
  override def name(): String = "sch"

  override def checkArgs(args: Validator.Arguments): Either[String, Unit] = args match {
    case Seq()                            => Left("Path to a Schematron schema required.")
    case s: Seq[_] if s.exists(schemaArg) => Right(Unit)
    case v                                => Left(s"Unsupported arguments, $v")

    // todo;; potentially load the schematron here and validate it, ie fail fast(er)
  }

  def validateXML(document: InputStream, errHandler: ErrorHandler, args: Seq[Validator.Argument]): Unit =
    args.find(schemaArg) match {
      case Some(Validator.Argument(_, path)) if path.endsWith(".sch") =>
        SchUtil.sch2xslt(Paths.get(path).toFile) match {
          case Right(sch) => validate(document, sch)
          case Left(e)    => throw new Exception(e)
        }
      case Some(Validator.Argument(_, path)) => validate(document, Source.fromFile(path).mkString)
      case None                              => println("KaBoom!")
    }

  private def validate(document: InputStream, sch: String) = {
    val res = SchematronResourceXSLT.fromString(sch, StandardCharsets.UTF_8);
    // todo;; shoud be validated up in checkArgs, and not again here
    if (!res.isValidSchematron)
      throw new Exception("invalid schematron")
    val svrl: SchematronOutputType = res.applySchematronValidationToSVRL(SchUtil.istream(document))
    SchUtil.printSvrl(svrl)
  }

  private def schemaArg(arg: Validator.Argument): Boolean = arg.key == "default" || arg.key == "schema"
}
