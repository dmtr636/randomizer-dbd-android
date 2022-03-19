package com.kodimstudio.myapplication.ui.stats;

import com.kodimstudio.myapplication.BuildConfig;

public class URLs {
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String RESOLVE_VANITY_URL = "https://api.steampowered.com/ISteamUser/ResolveVanityURL/v1/?key=";
    private static final String GET_PLAYER_SUMMARIES = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?key=";
    private static final String GET_PLAYER_STATS = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=381210&key=";

    public static String resolveVanityUrl(String steamUrl) {
        return RESOLVE_VANITY_URL + API_KEY + "&vanityurl=" + steamUrl;
    }

    public static String getPlayerSummaries(String steamId) {
        return GET_PLAYER_SUMMARIES + API_KEY + "&steamids=" + steamId;
    }

    public static String getPlayerStats(String steamId) {
        return GET_PLAYER_STATS + API_KEY + "&steamid=" + steamId;
    }
}
