package eu.badmood

import net.liftweb.http.SHtml
import net.liftweb.http.js.JE._
import net.liftweb.http.js.{JsExp, JsCmd}

object LiftUtils {

  def ajaxFunction1(functionName: String, func: (Any) => JsCmd) = {
    JsRaw("""function %1s(a){console.log(a);%2s}""".format(functionName,SHtml.jsonCall(JsRaw("a"), func)._2.toJsCmd))
  }

  def jQueryAttr(id:String,attribute:String,value:JsExp) = {
    JsRaw("""$('%1s').attr('%2s', %3s)""".format(id,attribute,value.toJsCmd)).cmd
  }

  def jQueryFadeTo(id:String,alpha:Double) = {
    JsRaw("""$('%1s').fadeTo(0, %2s)""".format(id,alpha.toString)).cmd
  }

}