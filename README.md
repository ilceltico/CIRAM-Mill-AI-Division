# CIRAM-Mill-AI-Division
An Artificial Intelligence that plays Nine Men's Morris.

This player participated in the [AI course yearly competition of University of Bologna](http://ai.unibo.it/node/597) in 2018.
CIRAMill won the competition with 10 wins out of 12, and 2 draws. No losses.
It also beat the reigning champion, effectively becoming the new champion of the competition.

## Usage
The player is designed to send its moves to the game server, which can also be found in this repo, as long as a client to eventually play as a human being against the AI.

1. If you don't have it, install Java 8.
2. Open your terminal and navigate to the directory where you have the jar files that are in the root of this repo. If you cloned the repo just navigate to your local directory of this repo.
3. Execute `java -jar server2018.jar`.
4. Execute firstly the white player and secondly the black player. For the white player you can choose `java -jar HumanWhite.jar` or `java -jar CIRAMill.jar white`, for the black player `java -jar HumanBlack.jar`,`java -jar CIRAMill.jar black`.
5. Play! Remember, even if you play as a human you still have only 60 seconds to think for your move!

## Technical requirements for the competition
* Max 60 seconds of processing time for each move
* Max 2GB of RAM
* Allowed to process during the opponent's turn (for move prediction, etc.)
* Any language is allowed
* Multithreading allowed, 3 cores available

## Approach and main features
**State space search, depth-first, iterative deepening approach.**

### Implemented:
* Different algorithms: MiniMax, Alpha-Beta pruning, NegaScout, MTDf in 2 variants
* Different pruning techniques: Killer Euristic, History Heuristic, Relative History Heuristic, Transposition Table
* [Belasius](http://belasius.com/mulino/) and [Petcu-Holban](http://www.dasconference.ro/papers/2008/B7.pdf) heuristics
* Quiescence
* Opening move-set for both colors
* Multithreaded search
* Idle-time search
* Symmetries recognition
* **Very efficient bitwise representations and operations for states and actions**

### Used:
* Minimax with AlphaBeta pruning.
* 2GB Transposition Table to have the best 2 moves for every cached state, with a particular implementation that is able to discriminate draws (because the state itself is not enough to describe the situation, you would actually need the whole graph that led to that state in order to make everything work correctly). This implementation is both efficient and correct, since draws are correctly recognized and eventually used, while not actually wasting memory to store the graph.
* History Heuristic with Butterfly Table to have a Relative History Heuristic that sorts all the remaining moves for each state.
* Modified Petcu-Holban heuristic.
* Opening move-sets computed by executing the iterative deepening algorithms overnight (LOL).
* Symmetries (4 spacial symmetries + color symmetry).
* Efficient bitwise stuff.
* Idle-time search only used to handicap the opponent in case they're running on the same machine. This is quite aggressive, so you can actually deactivate it with the `--noidle` option.

**In this repo you can also find a [PPT](https://github.com/ilceltico/CIRAM-Mill-AI-Division/raw/master/Presentazione%20-%20TEAM%20CIRAM.pptx) (also available as a [PDF](https://github.com/ilceltico/CIRAM-Mill-AI-Division/raw/master/Presentazione%20-%20TEAM%20CIRAM.pdf)) that we used to present the player to the class.
These files include some interesting charts showing the performances of the different algorithms we implemented.**
Disclaimer: due to a lack of time tests were very very limited, so these data are definitely not accurate, we know it! They're quite interesting to see, though!
