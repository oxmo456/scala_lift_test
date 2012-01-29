package eu.badmood.comet

import net.liftweb.http.CometActor
import xml.Text
import eu.badmood.avsbgame.AvsBGameStats
import eu.badmood.avsbgame.AvsBGameStats.{UserConnected, UserDisconnected}

class WatchConnectedUsers extends CometActor{

  override def localSetup(){
    AvsBGameStats ! UserConnected()
    super.localSetup()
  }

  override def localShutdown(){
    AvsBGameStats !  UserDisconnected()
    super.localShutdown()
  }

  def render = "*" #> Text("")

}