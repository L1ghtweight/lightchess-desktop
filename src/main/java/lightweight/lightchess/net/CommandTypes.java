package lightweight.lightchess.net;

public enum CommandTypes {
    msg, get_ip, list_clients, move, request_to_play, playrequest_accepted, get_board, play_black, play_white, signup, login,
    logout, update_gameboard,
    get_tournament_details,
    register_for_tournament,
    ready_to_play,
    start_tournament_match,
    tournament_match_end,
    casual_match_end,
    resign_from_match,
    score_board,
    login_response,
    signup_response,
    get_user_info,
    users_list,
    update_time_format,
    update_user_info,
}
