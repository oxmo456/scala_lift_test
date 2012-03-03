package eu.badmood.snippet

import net.liftweb.util.Helpers._
import eu.badmood.avsbgame.MicroChat
import net.liftweb.http.js.JsCmds.{SetValById}
import net.liftweb.http.{DispatchSnippet, SHtml}

class MicroChatInput extends DispatchSnippet{

  def dispatch = {
    case "render" => render
  }

  def render = "name=microChatInput" #> SHtml.onSubmit(s => {
    MicroChat ! MicroChat.Message(s)
    SetValById("microChatInput","")
  })

}