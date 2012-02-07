package eu.badmood.avsbgame


import collection.mutable.ArrayBuffer

import net.liftweb.actor._
import net.liftweb.http._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE._

import net.liftweb.json._

import eu.badmood.LiftUtils
import eu.badmood.avsbgame.AvsBGameStats.ScoresChange
import math.BigInt._


object AvsBGame extends LiftActor with ListenerManager {

  class Side(val value: Int);

  case class NoSide() extends Side(-1);

  case class SideA() extends Side(1);

  case class SideB() extends Side(0);

  case class Player(var side: Side)

  case class ChangeCellSide(currentPlayerSide: Side, cellIndex: Int)

  object currentPlayer extends SessionVar[Player](Player(NoSide()))

  case class CellChange(cellIndex: Int, side: Side)

  private val size = 10 * 10


  private val grid = ArrayBuffer[Side](ArrayBuffer.fill(size)(SideA()): _*)

  def getGrid = grid.clone()

  override protected def createUpdate = ()

  private def cellIndexIsValid(cellIndex: Int): Boolean = {
    cellIndex >= 0 && cellIndex < size
  }

  private def updateScores(currentPlayerSide : Side){
    grid.foldLeft[Int](0) {
      (sum, side) => sum + side.value
    } match {
      case x if x == size => Scores.sideAScore += 100
      case x if x == 0 => Scores.sideBScore += 100
      case _ => currentPlayerSide match {
        case SideA() => Scores.sideAScore += 1
        case SideB() => Scores.sideBScore += 1
      }
    }
  }

  override protected def lowPriority = {
    case ChangeCellSide(currentPlayerSide, cellIndex) if cellIndexIsValid(cellIndex) && currentPlayerSide != grid(cellIndex) => {
        grid(cellIndex) = currentPlayerSide
        updateScores(currentPlayerSide)
        updateListeners(CellChange(cellIndex, currentPlayerSide))
        AvsBGameStats ! ScoresChange(Scores.totalScore, Scores.sideAScore, Scores.sideBScore)
    }
  }


  private object Scores {
    var sideAScore = 0;
    var sideBScore = 0;
    def totalScore = sideAScore + sideBScore;
  }


  object Js {
    val gameVarName = "game"
    val spriteSheetURL = "images/html5vsflash.png"
    val spriteSheetWidth = "1000"
    val spriteSheetHeight = "500"
    val cellClickHandlerFuncName = "cellClickHandler"
    val setPlayerSideFuncName = "setPlayerSide"
    val cellChangeFuncName = "updateCell"

    def gameData = {
      val data = JArray(grid.map(side => JInt(side.value)).toList)
      compact(render(data))
    }

    def cellClickHandler = {
      LiftUtils.ajaxFunction1(cellClickHandlerFuncName, (data: Any) => {
        data match {
          case cellIndex: Double => AvsBGame ! ChangeCellSide(currentPlayer.side, cellIndex.toInt)
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
      JsRaw("""%1s.%2s(%3s,%4s)""".format(gameVarName, cellChangeFuncName, cellIndex.toString, side.value.toString)).cmd
    }

  }

}