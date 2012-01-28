package eu.badmood.avsbgame

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager


object MicroChat extends LiftActor with ListenerManager {

  case class Init()

  case class Message(value:String)



  def createUpdate = Init()

  override def lowPriority = {
    case Message(value) => updateListeners(Message(value))
    case _ => println("message !")
  }

}