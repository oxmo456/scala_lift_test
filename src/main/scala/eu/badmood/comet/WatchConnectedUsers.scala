package eu.badmood.comet

import net.liftweb.http.CometActor
import net.liftweb.util.Helpers._
import net.liftweb.common.Full
import xml.Text
import eu.badmood.avsbgame.AvsBGameStats
import eu.badmood.avsbgame.AvsBGameStats.{UserConnected, UserDisconnected}

class WatchConnectedUsers extends CometActor{

  override def lifespan = Full(10 seconds)

  override def localSetup(){
    super.localSetup()
    AvsBGameStats ! UserConnected()
  }

  override def localShutdown(){
    super.localShutdown()
    AvsBGameStats !  UserDisconnected()
  }

  def render = "*" #> Text("")

}