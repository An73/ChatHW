package main;

import db.DBService;

public class Main {

    public static void main (String[] args){
        try {
            DBService dbService = new DBService();
            dbService.create();
            Server server = new Server(dbService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
