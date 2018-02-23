public class studentAI extends Player {
    private int maxDepth;


    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void move(BoardState state) {
        move = alphabetaSearch(state,maxDepth);
    }

    public int alphabetaSearch(BoardState state, int maxDepth) {
    	int bestMove=0;
    	int maxValue=0;
    	int currentDepth = 0;
    	int alpha = Integer.MIN_VALUE;
    	int beta = Integer.MAX_VALUE;
    	maxValue = maxValue(state,maxDepth,currentDepth,alpha,beta);
    	for(int i=0;i<6;i++) {
    		if(maxForMoves[i]==maxValue) {
    			if(state.isLegalMove(1, i)) {
    				return i;    				
    			}
    		}
    	}
    	return bestMove;
    }
    private int[] maxForMoves = new int[6];
    
    public int maxValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {
    	int player = 1;
    	if(currentDepth==maxDepth || state.status(2)!=BoardState.GAME_NOT_OVER) {
    		return sbe(state);
    	}
    	else {
    		int maxValue = Integer.MIN_VALUE;
    		BoardState boardPrediction = state;
    		for(int i=0;i<6;i++) {
    			if(state.isLegalMove(player, i)) {
    				boardPrediction=state.applyMove(player, i);			
    				maxValue = Math.max( maxValue , minValue(boardPrediction,maxDepth,currentDepth+1,alpha,beta));
    				if(currentDepth==0) {
    					maxForMoves[i]=maxValue;
    				}
    				if(maxValue>=beta) {
    					return maxValue;
    				}
    				alpha = Math.max(alpha, maxValue);
    			}
    		}
    		return maxValue;
    	}
    }

    public int minValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {
    	int player = 2;
    	if(currentDepth==maxDepth || state.status(1)!=BoardState.GAME_NOT_OVER) {
    		return sbe(state);
    	}
    	else {
    		int minValue = Integer.MAX_VALUE;
    		BoardState boardPrediction = state;
    		for(int i=0;i<6;i++) {
    			if(state.isLegalMove(player, i)) {
    				boardPrediction=state.applyMove(player, i);			
    				minValue = Math.min( minValue , maxValue(boardPrediction,maxDepth,currentDepth+1,alpha,beta));
    				if(minValue<=alpha) {
    					return minValue;
    				}
    				beta = Math.min(beta, minValue);
    			}
    		}
    		return minValue;
    	}
    }

    public int sbe(BoardState state){
    	int stonePlayer = state.score[0]-state.score[1];
    	return stonePlayer;
    }


}