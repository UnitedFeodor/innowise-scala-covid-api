package com.innowise.router

import cats.effect.{Concurrent, Sync}
import cats.implicits.*
import com.innowise.entity.GeneralCaseData
import com.innowise.service.CovidService
import io.circe.{Decoder, Encoder}
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher

object CovidRouter:

  private object FromDateQueryParamMatcher extends QueryParamDecoderMatcher[String]("from")

  private object ToDateQueryParamMatcher extends QueryParamDecoderMatcher[String]("to")
  
  def getAllCountriesRoute[F[_] : Sync](covidApiService: CovidService[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "covid" / "countries" =>
        covidApiService.getAllCountries.flatMap { countryList =>
          Ok(countryList)
        }
    }
    
  def getMinMaxCasesForCountryRoute[F[_] : Sync](covidApiService: CovidService[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "covid" / "country" / country :?
        FromDateQueryParamMatcher(from) +& ToDateQueryParamMatcher(to) => {
          covidApiService.getMinMaxCovidCaseDataForPeriod(country, from, to).flatMap { minMaxCovidCaseList =>
            Ok(minMaxCovidCaseList)
          }
        }
    }
