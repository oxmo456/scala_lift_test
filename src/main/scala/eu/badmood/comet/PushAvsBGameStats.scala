package eu.badmood.comet

import net.liftweb.http.{CometListener, CometActor}
import eu.badmood.avsbgame.AvsBGameStats
import eu.badmood.avsbgame.AvsBGameStats.Stats
import net.liftweb.http.js.JsCmds.SetHtml
import xml.Text


class PushAvsBGameStats extends CometActor with CometListener {
  def registerWith = AvsBGameStats

  def render = {
    "#totalScore *" #> Text(AvsBGameStats.getTotalScore.toString) &
      "#sideAScore *" #> Text(AvsBGameStats.getSideAScore.toString) &
      "#sideBScore *" #> Text(AvsBGameStats.getSideBScore.toString) &
      "#connectedUsers *" #> Text(AvsBGameStats.getConnectedUsers.toString) &
      "#maxConnectedUsers *" #> Text(AvsBGameStats.getMaxConnectedUsers.toString)
  }

  override def lowPriority = {
    case Stats(totalScrore, sideAScore, sideBScore, connectedUsers, maxConnectedUsers) => {
      partialUpdate {
        SetHtml("#totalScore", Text(totalScrore.toString)) &
          SetHtml("#sideAScore", Text(sideBScore.toString)) &
          SetHtml("#sideBScore", Text(sideBScore.toString)) &
          SetHtml("#connectedUsers", Text(connectedUsers.toString)) &
          SetHtml("#maxConnectedUsers", Text(maxConnectedUsers.toString))
      }
    }
    case () => println("INIT !") //listener init
  }
}