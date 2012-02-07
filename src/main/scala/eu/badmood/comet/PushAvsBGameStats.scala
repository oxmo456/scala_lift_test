package eu.badmood.comet

import net.liftweb.http.{CometListener, CometActor}
import eu.badmood.avsbgame.AvsBGameStats
import eu.badmood.avsbgame.AvsBGameStats.StatsChange
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
    case StatsChange() => {
      partialUpdate {
        SetHtml("#totalScore", Text(AvsBGameStats.getTotalScore.toString)) &
          SetHtml("#sideAScore", Text(AvsBGameStats.getSideAScore.toString)) &
          SetHtml("#sideBScore", Text(AvsBGameStats.getSideBScore.toString)) &
          SetHtml("#connectedUsers", Text(AvsBGameStats.getConnectedUsers.toString)) &
          SetHtml("#maxConnectedUsers", Text(AvsBGameStats.getMaxConnectedUsers.toString))
      }
    }
  }
}