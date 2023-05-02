package com.innowise.service

import cats.effect.Concurrent
import cats.implicits._
import com.innowise.entity.{Country, GeneralCaseData, MinMaxCaseData}
import io.circe.{Decoder, Encoder}
import org.http4s
import org.http4s._
import org.http4s.Method._
import org.http4s.UriTemplate.{ParamElm, PathElm}
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits._

import java.time.ZonedDateTime
import java.time._
import java.time.format.DateTimeFormatter

class CovidService[F[_] : Concurrent](client: Client[F]):
  private final case class ServiceException(e: Throwable) extends RuntimeException

  private val CovidApiUri = uri"https://api.covid19api.com"

  private val dsl = new Http4sClientDsl[F] {}
  import dsl._

  def getAllCountries: F[List[Country]] =
    val requestUri = CovidApiUri / "countries"

    client.expect[List[Country]](requestUri)
      .adaptError { 
        case err =>
        ServiceException(err)
      }

  def getMinMaxCovidCaseDataForPeriod(country: String, from: String, to: String): F[MinMaxCaseData] =
    getAllCovidCasesForPeriod(country, from, to)
      .map(calculateMinMaxCases)

  private def getAllCovidCasesForPeriod(country: String, from: String, to: String): F[List[GeneralCaseData]] = 
    val requestUri = CovidApiUri / "country" / country /
      "status" / "confirmed" +?
      ("from", from) +?
      ("to", to)

    client.expect[List[GeneralCaseData]](requestUri)
      .map { covidCasesList =>
        covidCasesList.filter(_.province.isEmpty)
      }
      .adaptError { case err =>
        ServiceException(err)
      }

  private def calculateMinMaxCases(covidCasesList: List[GeneralCaseData]): MinMaxCaseData =
    var minNewCases = Int.MaxValue
    var maxNewCases = Int.MinValue

    var minNewCasesDate: ZonedDateTime = null
    var maxNewCasesDate: ZonedDateTime = null

    for (i <- 1 until covidCasesList.length) {
      val dailyCaseDifference = covidCasesList(i).cases - covidCasesList(i - 1).cases
      val newCases = if (dailyCaseDifference >= 0) dailyCaseDifference else 0

      if (newCases > maxNewCases) {
        maxNewCases = newCases
        maxNewCasesDate = covidCasesList(i).date
      }
      if (newCases < minNewCases) {
        minNewCases = newCases
        minNewCasesDate = covidCasesList(i).date
      }
    }

    MinMaxCaseData(covidCasesList.head.country, minNewCases, minNewCasesDate,
      maxNewCases, maxNewCasesDate)

