package com.brac.bracebatra.util;

import com.brac.bracebatra.model.Sync;

import org.json.JSONArray;

/**
 * Created by Amit on 12/14/2017.
 */

public interface IOnSyncListener {
    void syncDataToWeb(Sync sync,JSONArray jsonArray);
}
