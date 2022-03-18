package lightweight.lightchess.net;

public enum CommandTypes {
    msg, get_ip, list_clients, move, requestToPlay, playRequestAccecpted, getBoard, playBlack, playWhite, signup, login,
    list_loggedInClients, logout, updateGameBoard,
    get_tournament_details;
}
