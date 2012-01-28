package eu.badmood.snippet
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds._
import eu.badmood.avsbgame.AvsBGame


class InitJsGameEngine {

  def render = {

    "*" #> Script(AvsBGame.Js.cellClickHandler & AvsBGame.Js.initGame )

  }


}

