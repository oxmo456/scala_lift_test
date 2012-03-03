package eu.badmood.avsbgame

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import net.liftweb.util.Schedule
import net.liftweb.util.Helpers._


object AvsBGameStats extends LiftActor with ListenerManager {

  case class ScoresChange(totalScore: Int, sideAScore: Int, sideBScore: Int)

  case class Stats(totalScrore: Int, sideAScore: Int, sideBScore: Int, connectedUsers: Int, maxConnectedUsers: Int)

  case class UserConnected()

  case class UserDisconnected()

  private case class UpdateListeners()

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

  def getTotalScore = totalScore

  def getSideAScore = sideAScore

  def getSideBScore = sideBScore

  def getConnectedUsers = connectedUsers

  def getMaxConnectedUsers = maxConnectedUsers

  private val UPDATE_FREQUENCY = 10 seconds

  Schedule.schedule(this, UpdateListeners(), UPDATE_FREQUENCY)

  override protected def createUpdate: Any = Stats(totalScore, sideAScore, sideAScore, connectedUsers, maxConnectedUsers)

  override def lowPriority = {
    case ScoresChange(totalScore, sideAScore, sideBScore) => {
      this.totalScore = totalScore
      this.sideAScore = sideAScore
      this.sideBScore = sideBScore
    }
    case UserConnected() => {
      connectedUsers += 1
      maxConnectedUsers = math.max(connectedUsers, maxConnectedUsers);
    }
    case UserDisconnected() => {
      connectedUsers -= 1
    }
    case UpdateListeners() => {
      updateListeners(Stats(totalScore, sideAScore, sideAScore, connectedUsers, maxConnectedUsers))
      Schedule.schedule(this, UpdateListeners(), UPDATE_FREQUENCY)
    }
  }


}