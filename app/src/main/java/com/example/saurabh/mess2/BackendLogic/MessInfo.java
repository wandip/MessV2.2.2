package com.example.saurabh.mess2.BackendLogic;

/**
 * Created by saurabh on 2/5/17.
 */

public class MessInfo {
    String name;
    String mid;

    int plates;

    public MessInfo(String mid, String name) {
        this.mid = mid;

        this.name = name;

        //this.plates = plates;
    }

    @Override
    public String toString() {
        return "Mess{" +
                "mid=" + mid +
                ", name='" + name + '\'' +
                '}';
    }
}
