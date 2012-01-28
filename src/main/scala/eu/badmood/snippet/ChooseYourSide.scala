package eu.badmood.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE._
import net.liftweb.http.{SHtml}
import eu.badmood.LiftUtils._
import eu.badmood.avsbgame.AvsBGame._
import Js._

class ChooseYourSide {

  private def  buttonClickResponse(side:Side) = {

    def createResponse(selectorA:String,selectorB:String) = {
      jQueryAttr(selectorB,"disabled",JsNull) &
      jQueryAttr(selectorB,"onclick",JsNull) &
      jQueryAttr(selectorA,"onclick",JsNull) &
      jQueryFadeTo(selectorB,0.1) &
      setPlayerSide(side)
    }

    side match {
      case SideA() => createResponse("#chooseSideAButton","#chooseSideBButton")
      case SideB() => createResponse("#chooseSideBButton","#chooseSideAButton")
      case _ => Noop
    }

  }

  private def buttonClick(side:Side) = () => {
    currentPlayer.side match {
      case NoSide() => {
        currentPlayer.set(Player(side))
        buttonClickResponse(side)
      }
      case _ => Noop
    }
  }


  def render = currentPlayer.side match {
    case NoSide() => {
      "#chooseSideAButton [onclick]" #> SHtml.ajaxInvoke(buttonClick(SideA())) &
        "#chooseSideBButton [onclick]" #> SHtml.ajaxInvoke(buttonClick(SideB()))
    }
    case SideA() => {
      "#chooseSideBButton [disabled]" #> "true" & "#chooseSideBButton [style+]" #> "opacity:0.1;"
    }
    case SideB() => {
      "#chooseSideAButton [disabled]" #> "true" & "#chooseSideAButton [style+]" #> "opacity:0.1;"
    }
  }




}