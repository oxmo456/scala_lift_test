package eu.badmood.avsbgame

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import eu.badmood.avsbgame.AvsBGame.{SideB, SideA, Side}

object AvsBGameStats extends LiftActor with ListenerManager {

  case class ScoresChange(totalScore:Int,sideAScore:Int, sideBScore:Int)
  case class StatsChange()
  case class UserConnected()
  case class UserDisconnected()

  @volatile
  private var totalScore = 0

  @volatile
  private var sideAScore = 0

  @volatile
  private var sideBScore = 0

  @volatile
  private var connectedUsers = 0

  @volatile
  private var maxConnectedUsers = 0;

  override protected def createUpdate: Any = ()

  override def lowPriority = {
    case ScoresChange(totalScore,sideAScore,sideBScore) => {
      this.totalScore = totalScore
      this.sideAScore = sideAScore
      this.sideBScore = sideBScore
      updateListeners(StatsChange())
    }
    case UserConnected() => {
      connectedUsers += 1
      maxConnectedUsers = math.max(connectedUsers,maxConnectedUsers);
      updateListeners(StatsChange())
    }
    case UserDisconnected() => {
      connectedUsers -= 1
      updateListeners(StatsChange())
    }
  }

  def getTotalScore = totalScore

  def getSideAScore = sideAScore

  def getSideBScore = sideBScore

  def getConnectedUsers = connectedUsers

  def getMaxConnectedUsers = maxConnectedUsers
}