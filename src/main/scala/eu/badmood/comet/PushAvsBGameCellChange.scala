package eu.badmood.comet

import net.liftweb._
import http._
import eu.badmood.avsbgame.{AvsBGame}
import xml.Text
import eu.badmood.LiftUtils.MessageListener

class PushAvsBGameCellChange extends CometActor with MessageListener {

  override def cometActorMessageDispatcher = AvsBGame

  def render = "*" #> Text("")

  override def lowPriority = {
    case AvsBGame.CellChange(cellIndex, side) => {
      partialUpdate(AvsBGame.Js.cellChange(cellIndex,side));
    }
    case _ => ()
  }

}