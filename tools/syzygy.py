# Install (once): pip install python-chess

import chess
import chess.syzygy
from typing import List, Tuple

TB_PATHS = [
    "/home/arco/syzygy",   # adjust to your system
    #"/path/to/syzygy/6-men",   # optional
    # add more directories as needed
]

def open_tablebase(paths):
    # Create empty tablebase manager, then add dirs (lets you add multiple roots).
    tb = chess.syzygy.Tablebase()
    for p in paths:
        try:
            tb.add_directory(p)
        except FileNotFoundError:
            pass  # ignore missing paths
    return tb

def classify_moves_with_wdl(tb: chess.syzygy.Tablebase, board: chess.Board) -> List[Tuple[chess.Move, int]]:
    """Return list of (move, wdl_score) from side-to-move perspective."""
    results = []
    stm = board.turn  # True if White to move
    for move in board.legal_moves:
        board.push(move)
        try:
            wdl_child = tb.probe_wdl(board)
        except KeyError:
            # Table not available for this child position; mark as None
            wdl_child = None
        board.pop()

        if wdl_child is None:
            results.append((move, None))
            continue

        # wdl_child is from the POV of the *side to move in the child* (which is the opponent now),
        # so invert the sign for our current side.
        wdl_from_stm = -wdl_child
        results.append((move, wdl_from_stm))
    return results

def pick_best_moves(wdl_results: List[Tuple[chess.Move, int]]):
    """Given WDL scores from current side POV, choose best (max) non-None entries."""
    filtered = [(m, s) for (m, s) in wdl_results if s is not None]
    if not filtered:
        return []  # no TB info
    best_score = max(s for _, s in filtered)
    best_moves = [m for m, s in filtered if s == best_score]
    return best_score, best_moves

def optional_root_dtz(tb: chess.syzygy.Tablebase, board: chess.Board):
    """Probe DTZ if available; may raise KeyError."""
    return tb.probe_dtz(board)

if __name__ == "__main__":
    # Example: a known won KQ vs KR position (Black to move)
    fen = "8/8/8/8/8/8/kq6/6KQ w - - 0 1"
    board = chess.Board(fen)

    with open_tablebase(TB_PATHS) as tb:
        try:
            wdl_here = tb.probe_wdl(board)
            print(f"Current position WDL (TB POV): {wdl_here}")
        except KeyError:
            print("Current position not found in tablebase.")

        # Classify each legal move by probing child
        wdl_results = classify_moves_with_wdl(tb, board)
        best = pick_best_moves(wdl_results)
        if best:
            best_score, best_moves = best
            label = {2: "Win", 1: "Cursed Win", 0: "Draw", -1: "Blessed Loss", -2: "Loss"}.get(best_score, str(best_score))
            print(f"Best outcome achievable: {label} ({best_score})")
            print("Moves that preserve it:")
            for m in best_moves:
                print("  ", board.san(m))
        else:
            print("No tablebase info for any child moves.")

        # Optional: refine with DTZ (if present) to pick fastest progress-to-zeroing line
        try:
            dtz_here = optional_root_dtz(tb, board)
            print(f"DTZ value: {dtz_here} (plies to zeroing; sign encodes result class)")
        except KeyError:
            pass
        print(board)
