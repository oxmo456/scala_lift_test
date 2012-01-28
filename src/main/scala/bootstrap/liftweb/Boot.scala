package bootstrap.liftweb

import net.liftweb._
import http._
import sitemap.{SiteMap, Menu, Loc}
import util.{ NamedPF }


class Boot {
  def boot {
  
    LiftRules.addToPackages("eu.badmood")



    val entries = (List(Menu("Home") / "index") :::
                  Nil)
    
    LiftRules.uriNotFound.prepend(NamedPF("404handler"){
      case (req,failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions","404"),"html",false,false))
    })
    
    LiftRules.setSiteMap(SiteMap(entries:_*))
    
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))



  }
}