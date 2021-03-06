package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import chess.engine.Colors.Colors;
import chess.engine.board.Board;
import chess.engine.board.Movement;
import chess.engine.board.Tile;
import chess.engine.board.Utility;
import chess.engine.pieces.Piece.PieceType;

public class King extends Piece {

	private static final int[] MOVE_COORDINATES = {-8, -1, 1, 8, -9, 9, -7, 7};
	
	public King(final int piecePosition, final Colors pieceColor) {
		super(piecePosition, pieceColor, PieceType.KING);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Movement> calculateMoves(Board board) {
		// TODO Auto-generated method stub
		int pieceCoordinate;
		
		final List<Movement>legalMoves = new ArrayList<>();
		
		for (final int currentOffset : MOVE_COORDINATES) {
			pieceCoordinate = this.piecePosition + currentOffset;
			
			//if move is valid, piece will move to that location under the condition....
			if(Utility.isValidCoordinate(pieceCoordinate)) {
				
				if (firstColumnExclusion(this.piecePosition, currentOffset) || eightColumnExclusion(this.piecePosition, currentOffset)) {
					continue;
				}
				
				final Tile destinationTile = board.getTile(pieceCoordinate);
				//...if the tile is not occupied piece will move to empty tile
				if(!destinationTile.isTileOccupied()) {
					//normal movement
					legalMoves.add(new Movement.StandardMovement(board, this, pieceCoordinate));
				}
				
				//...otherwise we check what piece is on that tile
				//if the piece is the opposite color than we eat the piece
				else {
					final Piece pieceDestination = destinationTile.getPiece();
					final Colors pieceColor = pieceDestination.getPieceColor();
					
					if (this.pieceColor != pieceColor) {
						//attacking movement
						legalMoves.add(new Movement.AttackMovement(board, this, pieceCoordinate, pieceDestination));
					}
				}			
			}	
		}
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}
	
	//check edge cases
	private static boolean firstColumnExclusion(final int currentPosition, final int offset) {
		
		//if rook piece is in the first column and following knight piece moves were being considered 
		//it will not work
		return Utility.FIRST_COLUMN[currentPosition] && (offset == -1) || (offset == 7) || (offset == -9);
	}
	
	private static boolean eightColumnExclusion(final int currentPosition, final int offset) {
		
		//if rook piece is in the second column and the following knight piece moves were being considered
		//it will not work
		return Utility.EIGHT_COLUMN[currentPosition] && (offset == 1) || (offset == -7) || (offset == 9);
	}
	
	@Override
	public Piece movePiece(final Movement movement) {
		// TODO Auto-generated method stub
		return new King(movement.getDestination(), movement.getMovePiece().getPieceColor());
	}

}
