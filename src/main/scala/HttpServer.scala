package com.innowise

import cats.effect.{Async, IOApp}
import cats.syntax.all.*
import com.comcast.ip4s.*
import com.innowise.router.CovidRouter
import com.innowise.service.CovidService
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger
import scala.concurrent.ExecutionContext.global
import cats._
import cats.effect._
import org.http4s.server._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._

object HttpServer:

  def run[F[_] : Async]: F[Nothing] =
    EmberClientBuilder.default[F].build.flatMap { client =>
      val covidCasesAlg = CovidService[F](client)
      val httpApp = (
          CovidRouter.getMinMaxCasesForCountryRoute[F](covidCasesAlg) 
          <+> CovidRouter.getAllCountriesRoute[F](covidCasesAlg)
        ).orNotFound

      EmberServerBuilder.default[F]
        .withHost(ipv4"127.0.0.1")
        .withPort(port"8080")
        .withHttpApp(httpApp)
        .build
    }.useForever

