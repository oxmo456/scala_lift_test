package eu.badmood.avsbgame

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import net.liftweb.util.Schedule
import net.liftweb.util.Helpers._


object AvsBGameStats extends LiftActor with ListenerManager {

  case class ScoresChange(totalScore:Int,sideAScore:Int, sideBScore:Int)
  case class Stats(totalScrore:Int, sideAScore:Int,sideBScore:Int,connectedUsers:Int,maxConnectedUsers:Int)
  case class UserConnected()
  case class UserDisconnected()
  private case class UpdateListeners()

  private var totalScore = 0

  private var sideAScore = 0

  private var sideBScore = 0

  private var connectedUsers = 0

  private var maxConnectedUsers = 0;


  println("INIT SCHEDULE !")
  Schedule.schedule(this,UpdateListeners(),30 seconds)

  override protected def createUpdate: Any = Stats(totalScore,sideAScore,sideAScore,connectedUsers,maxConnectedUsers)

  override def lowPriority = {
    case ScoresChange(totalScore,sideAScore,sideBScore) => {
      this.totalScore = totalScore
      this.sideAScore = sideAScore
      this.sideBScore = sideBScore
    }
    case UserConnected() => {
      connectedUsers += 1
      maxConnectedUsers = math.max(connectedUsers,maxConnectedUsers);
    }
    case UserDisconnected() => {
      connectedUsers -= 1
    }
    case UpdateListeners() => {
      updateListeners(Stats(totalScore,sideAScore,sideAScore,connectedUsers,maxConnectedUsers))
      Schedule.schedule(this,UpdateListeners(),30 seconds)
    }
  }

}