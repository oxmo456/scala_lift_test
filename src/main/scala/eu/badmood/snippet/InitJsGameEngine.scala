package eu.badmood.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds._
import eu.badmood.avsbgame.AvsBGame
import net.liftweb.http.DispatchSnippet


object InitJsGameEngine extends DispatchSnippet {

  def dispatch = {
    case "render" => render
  }

  def render = {
    "*" #> Script(AvsBGame.Js.cellClickHandler & AvsBGame.Js.initGame)
  }


}

