package eu.badmood.comet

import net.liftweb._
import http._
import js.JE.{JsRaw, Str}
import eu.badmood.avsbgame.MicroChat
import xml.Text

class PushMicroChatLastMessage extends CometActor with CometListener {

  override def registerWith = MicroChat

  override def lowPriority = {
    case MicroChat.Message(value) => {
      partialUpdate {
        JsRaw(""" var chatHistory = $("#microChatHistory").html();
                  $("#microChatHistory").html( chatHistory + "\n" + %1s);
                  $("#microChatHistory").scrollTop(99999)""".format(Str(Text(value).toString()).toJsCmd)).cmd
      }
    }
    case MicroChat.Init() => ()
  }


  def render = "#microChatHistory *" #> "hello !"

}