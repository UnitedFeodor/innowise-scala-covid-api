package com.innowise.entity

import cats.effect.Concurrent
import cats.implicits.*
import com.innowise.entity.GeneralCaseData
import io.circe.{Decoder, Encoder}
import org.http4s.*
import org.http4s.circe.*

import java.time.ZonedDateTime

case class GeneralCaseData(country: String, province: String, cases: Int, date: ZonedDateTime)

object GeneralCaseData:
  given Decoder[GeneralCaseData] = Decoder.instance { field =>
    for {
      country <- field.get[String]("Country")
      province <- field.get[String]("Province")
      cases <- field.get[Int]("Cases")
      date <- field.get[ZonedDateTime]("Date")
    } yield GeneralCaseData(country, province, cases, date)
  }
  given Decoder[List[GeneralCaseData]] = Decoder.decodeList[GeneralCaseData]
  given[F[_] : Concurrent]: EntityDecoder[F, GeneralCaseData] = jsonOf
  given[F[_] : Concurrent]: EntityDecoder[F, List[GeneralCaseData]] = jsonOf
  

  given Encoder[GeneralCaseData] = Encoder.AsObject.derived[GeneralCaseData]
  given Encoder[List[GeneralCaseData]] = Encoder.encodeList[GeneralCaseData]
  given[F[_]]: EntityEncoder[F, GeneralCaseData] = jsonEncoderOf
  given[F[_]]: EntityEncoder[F, List[GeneralCaseData]] = jsonEncoderOf
  
  
