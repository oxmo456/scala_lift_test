package eu.badmood

import net.liftweb.http.js.JE._
import net.liftweb.http.js.{JsExp, JsCmd}
import net.liftweb.http.{CometActor, SHtml}

object LiftUtils {

  def ajaxFunction1(functionName: String, func: (Any) => JsCmd) = {
    JsRaw("""function %1s(a){%2s}""".format(functionName, SHtml.jsonCall(JsRaw("a"), func)._2.toJsCmd))
  }

  def jQueryAttr(id: String, attribute: String, value: JsExp) = {
    JsRaw("""$('%1s').attr('%2s', %3s)""".format(id, attribute, value.toJsCmd)).cmd
  }

  def jQueryFadeTo(id: String, alpha: Double) = {
    JsRaw("""$('%1s').fadeTo(0, %2s)""".format(id, alpha.toString)).cmd
  }


  trait MessageDispatcher {

    type Listener = { def !(msg:Any):Unit }

    private var listeners: List[Listener] = Nil

    def addListener(listener: Listener) {
      listeners = listeners :+ listener
      println("listeners " + listeners)
    }

    def removeListener(listener: Listener) {
      listeners = listeners.filterNot(_ == listener)
    }

    def dispatch(message: Any) {
      listeners.foreach(_ ! message)
    }

  }

  trait MessageListener extends CometActor {

    def cometActorMessageDispatcher: MessageDispatcher

    override def localSetup() {
      cometActorMessageDispatcher.addListener(this)
      super.localSetup()
    }

    override def localShutdown() {
      cometActorMessageDispatcher.removeListener(this)
      super.localShutdown()
    }

  }

}