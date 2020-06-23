/******************************************************************************
 *                             BombSweeper 1.0                                *
 *                  Copyright © 2015 Ben Goldsworthy (rumps)                  *
 *                                                                            *
 * A program to emulate the classic game Minesweeper                          *
 *                                                                            *
 * This file is part of BombSweeper.                                          *
 *                                                                            *
 * BombSweeper is free software: you can redistribute it and/or modify        *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation, either version 3 of the License, or          *
 * (at your option) any later version.                                        *
 *                                                                            *
 * BombSweeper is distributed in the hope that it will be useful,             *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 * GNU General Public License for more details.                               *
 *                                                                            *
 * You should have received a copy of the GNU General Public License          *
 * along with BombSweeper.  If not, see <http://www.gnu.org/licenses/>.       *
 ******************************************************************************/
  
/**
 ** This class provides a the square logic needed for a game of BombSweeper.
 **/

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 **   @author  Ben Goldsworthy (rumps) <me+bombsweeper@bengoldsworthy.net>
 **   @version 1.0
 **/
public class SmartSquare extends GameSquare {
	private boolean thisSquareHasBomb = false;
	public static final int MINE_PROBABILITY = 10;
   public boolean clicked = false;
   public boolean flagged = false;
  
   /**
    **   Constructor method. Creates a SmartSquare instance.
    **
    **   @param x The x-coord of the SmartSquare.
    **   @param y The y-coord of the SmartSquare.
    **   @param The GameBoard the SmartSqaure resides on.
    **/
	public SmartSquare(int x, int y, GameBoard board) {
		super(x, y, "images/blank.png", board);

		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
	}

   /**
    **   Called on square click.
    **/
	public void clicked() {
      this.clicked = true;
      
      // If it's a bomb, marks it as such
      if (thisSquareHasBomb) {
         this.setImage("images/bomb.png");
         return;
      }
      
      int thisX = this.xLocation;
      int thisY = this.yLocation;
      int bombs = 0;
      
      // Otherwise, check adjacent squares for bombs
      for (int x = thisX - 1; x < thisX + 2; x++) {
         for (int y = thisY - 1; y < thisY + 2; y++) {
            try {
               SmartSquare currSquare = (SmartSquare)board.getSquareAt(x, y);
               if (currSquare.hasBomb()) bombs++;
            } catch (NullPointerException e) {}
         }
      }
      // If there are any, set the square's image as the number of them
      this.setImage("images/" + bombs + ".png");
      // If not, simulate a click on all adjacent squares, recursively
      if (bombs == 0) {
         for (int x = thisX - 1; x < thisX + 2; x++) {
            for (int y = thisY - 1; y < thisY + 2; y++) {
               try {
                  SmartSquare currSquare = (SmartSquare)board.getSquareAt(x, y);
                  if (!currSquare.clicked) currSquare.clicked();
               } catch (NullPointerException e) {}
            }
         }
      }
	}
   
   
   /**
    **   Flags a square if right-clicked on.
    **/
   public void rightClicked() {
      flagged = !flagged;
      String img = flagged ? "flag" : "blank";
      this.setImage("images/"+img+".png");
   }
   
   /**
    **  Returns whether a square has a bomb on it or not.
    **/
   public boolean hasBomb() {
      return this.thisSquareHasBomb;
   }
}
