package com.innowise
package entity

import cats.effect.Concurrent
import cats.implicits.*
import io.circe.{Decoder, DecodingFailure, Encoder}
import org.http4s.*
import org.http4s.circe.*

case class Country(country: String, slug: String, iso2: String)

object Country:
  given Decoder[Country] = Decoder.instance { field =>
    for {
      country <- field.get[String]("Country")
      slug <- field.get[String]("Slug")
      iso2 <- field.get[String]("ISO2")
    } yield Country(country, slug, iso2)
  }
  given Decoder[List[Country]] = Decoder.decodeList[Country]
  given[F[_] : Concurrent]: EntityDecoder[F, Country] = jsonOf
  given[F[_] : Concurrent]: EntityDecoder[F, List[Country]] = jsonOf

  given Encoder[Country] = Encoder.AsObject.derived[Country]
  given Encoder[List[Country]] = Encoder.encodeList[Country]
  given[F[_]]: EntityEncoder[F, Country] = jsonEncoderOf
  given[F[_]]: EntityEncoder[F, List[Country]] = jsonEncoderOf
