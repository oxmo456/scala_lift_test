package eu.badmood.snippet

import net.liftweb.util.Helpers._
import net.liftweb.util.Props
import net.liftweb.http.DispatchSnippet

object LiftMode extends DispatchSnippet {

  def dispatch = {
    case "render" => render
  }

  def render = "*" #> ("(" + Props.mode.toString + ")")
}