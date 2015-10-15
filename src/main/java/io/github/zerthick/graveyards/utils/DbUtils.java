/*
 * Copyright (C) 2015  Zerthick
 *
 * This file is part of Graveyards.
 *
 * Graveyards is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Graveyards is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graveyards.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.graveyards.utils;

import com.flowpowered.math.vector.Vector3i;
import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DbUtils {

    public static void writeGraveyards(Map<UUID, Set<Graveyard>> graveyardMap) {

        Connection c;
        Statement stmt;

        if (!graveyardMap.isEmpty()) {
            try {
                // Create sqlite connection
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:Graveyards.db");
                stmt = c.createStatement();

                //Clean DB of old worlds and graveyards
                cleanDB(c, graveyardMap);

                // Iterate through graveyard map and add each graveyard to the db
                for (UUID worldUUID : graveyardMap.keySet()) {

                    String sql = "CREATE TABLE IF NOT EXISTS '" +
                            worldUUID.toString() +
                            "'(NAME TEXT PRIMARY KEY     NOT NULL," +
                            "X                  INT     NOT NULL," +
                            "Y                  INT     NOT NULL," +
                            "Z                  INT     NOT NULL)";
                    stmt.executeUpdate(sql);

                    Set<Graveyard> graveyardSet = graveyardMap.get(worldUUID);
                    for (Graveyard graveyard : graveyardSet) {
                        String graveyardName = graveyard.getName();
                        Vector3i graveyardLoc = graveyard.getLocation();

                        sql = "INSERT OR REPLACE INTO '" + worldUUID.toString() +"' (NAME,X,Y,Z) " +
                                "VALUES ('" + graveyardName + "',"
                                + graveyardLoc.getX() + ',' + graveyardLoc.getY() + ',' + graveyardLoc.getZ() + ");";
                        stmt.executeUpdate(sql);
                    }
                }

                stmt.close();
                c.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static Map<UUID, Set<Graveyard>> readGraveyards() {

        Map<UUID, Set<Graveyard>> graveyardMap = new HashMap<>();

        Connection c;
        Statement stmt;

        try {
            // Create sqlite connection
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Graveyards.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();

            // Get list of tables in db
            ResultSet tableNames = c.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
            while (tableNames.next()) {
                String tableName = tableNames.getString("TABLE_NAME");
                ResultSet graveyardRecords = stmt.executeQuery("SELECT * FROM '" + tableName + "';");

                // Iterate through table and add each graveyard to the map
                while (graveyardRecords.next()) {
                    String graveyardName = graveyardRecords.getString("NAME");
                    UUID worldUUID = UUID.fromString(tableName);
                    Vector3i graveyardLocation = new Vector3i(graveyardRecords.getInt("X"), graveyardRecords.getInt("Y"), graveyardRecords.getInt("Z"));

                    Set<Graveyard> graveyardSet = graveyardMap.getOrDefault(worldUUID, new HashSet<>());
                    graveyardSet.add(new Graveyard(graveyardName, graveyardLocation));
                    graveyardMap.put(worldUUID, graveyardSet);
                }
            }
            stmt.close();
            c.close();
        } catch (Exception ignore) {
        }
        return graveyardMap;
    }

    private static void cleanDB(Connection c, Map<UUID, Set<Graveyard>> graveyardMap) throws SQLException {

        Statement stmt = c.createStatement();

        Set<UUID> worldUUIDs = graveyardMap.keySet();
        ResultSet tableNames = c.getMetaData().getTables(null, null, null, new String[]{"TABLE"});
        Set<String> tablesToDrop = new HashSet<>();
        while(tableNames.next()){
            String tableName = tableNames.getString("TABLE_NAME");
            UUID worldUUID = UUID.fromString(tableName);
            if(!worldUUIDs.contains(worldUUID)){ //Mark world to be destroyed
                tablesToDrop.add(tableName);
            } else { //Clean table for old Graveyards
                Set<String> graveyardNames = graveyardMap.get(worldUUID).stream().map(Graveyard::getName).collect(Collectors.toSet());
                ResultSet graveyardRecords = stmt.executeQuery("SELECT * FROM '" + tableName + "';");
                while (graveyardRecords.next()){
                    String recordName = graveyardRecords.getString("NAME");
                    if(!graveyardNames.contains(recordName)){
                        stmt.executeUpdate("DELETE FROM '" + tableName + "' WHERE NAME = '" + recordName + "';");
                    }
                }
            }
        }
        // Clean old tables
        for(String table : tablesToDrop){
            stmt.executeUpdate("DROP TABLE '" + table +"';");
        }
        stmt.close();
    }
}
