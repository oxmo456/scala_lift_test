package eu.badmood.snippet

import net.liftweb.util.Helpers._
import net.liftweb.util.Props

class LiftMode {
  def render = "*" #> ("(" + Props.mode.toString + ")")
}