package com.innowise
package entity

import cats.effect.Concurrent
import cats.implicits.*
import com.innowise.entity.GeneralCaseData
import com.innowise.entity.MinMaxCaseData
import io.circe.*
import org.http4s.*
import org.http4s.circe.*

import java.time.ZonedDateTime

case class MinMaxCaseData(country: String, minCases: Int, minCasesDate: ZonedDateTime,
                       maxCases: Int, maxCasesDate: ZonedDateTime)

object MinMaxCaseData:
  given Decoder[MinMaxCaseData] = Decoder.derived[MinMaxCaseData]

  given[F[_] : Concurrent]: EntityDecoder[F, MinMaxCaseData] = jsonOf

  given Encoder[MinMaxCaseData] = Encoder.AsObject.derived[MinMaxCaseData]

  given[F[_]]: EntityEncoder[F, MinMaxCaseData] = jsonEncoderOf