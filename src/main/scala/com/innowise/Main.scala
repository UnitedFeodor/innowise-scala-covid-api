package com.innowise

import cats.effect.{IO, IOApp}
import com.innowise.server.CovidServer

object Main extends IOApp.Simple:
  val run = CovidServer.run[IO]
