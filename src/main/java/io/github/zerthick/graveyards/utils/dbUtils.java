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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class dbUtils {

    public static void writeGraveyards(Map<UUID, Set<Graveyard>> graveyardMap) {
        Connection c;
        Statement stmt;

        if (!graveyardMap.isEmpty()) {
            try {
                // Create sqlite connection
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:Graveyards.db");
                stmt = c.createStatement();

                // Remove table if it already exists and create a new one
                String sql = ("DROP TABLE IF EXISTS GRAVEYARDS");
                stmt.executeUpdate(sql);

                sql = "CREATE TABLE GRAVEYARDS" +
                        "(NAME TEXT PRIMARY KEY     NOT NULL," +
                        "WORLDID            INT     NOT NULL" +
                        "X                  INT     NOT NULL" +
                        "Y                  INT     NOT NULL" +
                        "Z                  INT     NOT NULL)";
                stmt.executeUpdate(sql);

                // Iterate through graveyard map and add each graveyard to the db
                for (UUID worldUUID : graveyardMap.keySet()) {
                    Set<Graveyard> graveyardSet = graveyardMap.get(worldUUID);

                    Iterator<Graveyard> it = graveyardSet.iterator();
                    while (it.hasNext()) {
                        Graveyard graveyard = it.next();
                        String graveyardName = graveyard.getName();
                        Vector3i graveyardLoc = graveyard.getLocation();

                        sql = "INSERT INTO GRAVEYARDS (NAME, WORLDID, X, Y, Z)" +
                                "VALUES (" + graveyardName + ',' + worldUUID + ','
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM GRAVEYARDS;");

            // Iterate through db and add each graveyard to the map
            while (rs.next()) {
                String graveyardName = rs.getString("name");
                UUID worldUUID = UUID.fromString(rs.getString("worldid"));
                Vector3i graveyardLocation = new Vector3i(rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));

                Set<Graveyard> graveyardSet = graveyardMap.getOrDefault(worldUUID, new HashSet<>());
                graveyardSet.add(new Graveyard(graveyardName, graveyardLocation));
                graveyardMap.put(worldUUID, graveyardSet);
            }

            stmt.close();
            c.close();
        } catch (Exception ignore) {
        }
        return graveyardMap;
    }
}
