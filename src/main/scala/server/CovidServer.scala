package com.innowise
package server

import router.CovidRouter
import service.CovidService

import cats.*
import cats.effect.*
import cats.syntax.all.*
import com.comcast.ip4s.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.*
import org.http4s.implicits.*
import org.http4s.server.*
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object CovidServer:

  def run[F[_] : Async]: F[Nothing] =
    EmberClientBuilder.default[F].build.flatMap { client =>
      val covidService = CovidService[F](client)
      val httpApp = (
          CovidRouter.getMinMaxCasesForCountryRoute[F](covidService) 
          <+> CovidRouter.getAllCountriesRoute[F](covidService)
        ).orNotFound

      EmberServerBuilder.default[F]
        .withHost(ipv4"127.0.0.1")
        .withPort(port"8080")
        .withHttpApp(httpApp)
        .build
    }.useForever

