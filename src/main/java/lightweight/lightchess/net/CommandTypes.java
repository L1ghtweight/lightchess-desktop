package lightweight.lightchess.net;

public enum CommandTypes {
    msg, get_ip, list_clients, move, request_to_play, playrequest_accepted, get_board, play_black, play_white, signup, login,
    list_loggedInClients, logout, update_gameboard,
    get_tournament_details,
    register_for_tournament,
    ready_to_play,
    start_tournament_match,
    tournament_match_end,
    get_score_board,
    login_success,
    login_failure,
    signup_success,
    signup_failure,
}
