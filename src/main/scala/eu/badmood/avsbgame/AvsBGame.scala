package eu.badmood.avsbgame


import collection.mutable.ArrayBuffer

import net.liftweb.actor.LiftActor
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE._
import net.liftweb.http.{ListenerManager, SessionVar}

import eu.badmood.LiftUtils
import eu.badmood.avsbgame.AvsBGameStats.IncreaseTotalScore


object AvsBGame extends LiftActor with ListenerManager {

  class Side(val value: Int);

  case class NoSide() extends Side(-1);

  case class SideA() extends Side(1);

  case class SideB() extends Side(0);

  case class Player(var side: Side)

  object currentPlayer extends SessionVar[Player](Player(NoSide()))

  case class CellChange(cellIndex: Int, side: Side)

  override protected def createUpdate: Any = ()

  private val size = 6 * 6

  private val grid = ArrayBuffer[Side](ArrayBuffer.fill(size)(SideA()): _*)


  def getGrid = grid.clone()

  def cellIndexIsValid(cellIndex: Int): Boolean = {
    cellIndex >= 0 && cellIndex < size
  }

  private def changeCellSide(cellIndex : Int) : Boolean = {
      val currentPlayerSide = currentPlayer.side
      if (currentPlayerSide != grid(cellIndex)) {
        grid(cellIndex) = currentPlayerSide
        updateListeners(CellChange(cellIndex, currentPlayerSide))
        AvsBGameStats ! IncreaseTotalScore(currentPlayerSide)
        true
      } else false
  }

  object Js {

    val gameVarName = "game"
    val spriteSheetURL = "images/html5vsflash.png"
    val spriteSheetWidth = "1000"
    val spriteSheetHeight = "500"
    val cellClickHandlerFuncName = "cellClickHandler"
    val setPlayerSideFuncName = "setPlayerSide"
    val cellChangeFuncName = "updateCell"

    def gameData = "[" + grid.map(_.value).mkString(",") + "]"

    def cellClickHandler = {
      LiftUtils.ajaxFunction1(cellClickHandlerFuncName, (data: Any) => {
        data match {
          case cellIndex: Double if cellIndexIsValid(cellIndex.toInt) => changeCellSide(cellIndex.toInt)
          case _ => ()
        }
        Noop
      })

    }

    def initGame = {
      JsRaw("""var %1s =  new AvsBGame("%2s",%3s,%4s,%5s,%6s,%7s);""".format(gameVarName,
        spriteSheetURL,
        spriteSheetWidth,
        spriteSheetHeight,
        gameData,
        cellClickHandlerFuncName,
        currentPlayer.side.value.toString))
    }

    def setPlayerSide(side: Side) = {
      JsRaw("""%1s.%2s(%3s)""".format(gameVarName, setPlayerSideFuncName, side.value.toString)).cmd
    }

    def cellChange(cellIndex: Int, side: Side) = {
      JsRaw("""%1s.%2s(%3s,%4s);""".format(gameVarName, cellChangeFuncName, cellIndex.toString, side.value.toString)).cmd
    }


  }

}