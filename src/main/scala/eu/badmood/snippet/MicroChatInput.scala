package eu.badmood.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import eu.badmood.avsbgame.MicroChat
import xml.{Text, NodeSeq}
import net.liftweb.http.js.JsCmds.{SetValById, SetValueAndFocus, SetHtml, Alert}

class MicroChatInput {

  def render = "name=microChatInput" #> SHtml.onSubmit(s => {
    MicroChat ! MicroChat.Message(s)
    SetValById("microChatInput","")
  })

}