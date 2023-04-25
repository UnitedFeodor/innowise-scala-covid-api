package com.innowise
package entity

import cats.effect.Concurrent
import cats.implicits.*
import com.innowise.entity.GeneralCaseData
import io.circe.{Decoder, Encoder}
import org.http4s.*
import org.http4s.circe.*

import java.time.ZonedDateTime

case class GeneralCaseData(country: String, province: String, cases: Int, date: ZonedDateTime)

object GeneralCaseData:
  given Decoder[GeneralCaseData] = Decoder.instance { h =>
    for {
      country <- h.get[String]("Country")
      province <- h.get[String]("Province")
      cases <- h.get[Int]("Cases")
      date <- h.get[ZonedDateTime]("Date")
    } yield GeneralCaseData(country, province, cases, date)
  }

  given[F[_] : Concurrent]: EntityDecoder[F, GeneralCaseData] = jsonOf

  given Encoder[GeneralCaseData] = Encoder.AsObject.derived[GeneralCaseData]

  given[F[_]]: EntityEncoder[F, GeneralCaseData] = jsonEncoderOf

  given Decoder[List[GeneralCaseData]] = Decoder.decodeList[GeneralCaseData]

  given[F[_] : Concurrent]: EntityDecoder[F, List[GeneralCaseData]] = jsonOf

  given Encoder[List[GeneralCaseData]] = Encoder.encodeList[GeneralCaseData]

  given[F[_]]: EntityEncoder[F, List[GeneralCaseData]] = jsonEncoderOf
